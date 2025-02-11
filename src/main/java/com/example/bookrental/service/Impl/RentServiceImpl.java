package com.example.bookrental.service.Impl;

import com.example.bookrental.model.domain.RentModel;
import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.entity.Rents;
import com.example.bookrental.model.entity.Users;
import com.example.bookrental.model.repository.BookRepository;
import com.example.bookrental.model.repository.RentRepository;
import com.example.bookrental.model.repository.UserRepository;
import com.example.bookrental.service.RentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public RentServiceImpl(RentRepository rentRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.rentRepository = rentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    //대출 등록(책이 등록되면 자동으로 실행되게)
    @Override
    public Object insert(Long id){
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 책이 존재하지 않습니다."));

        Rents rent = Rents.builder()
                .book(book)
                .build();
        Rents savedRent = rentRepository.save(rent);

        return RentModel.fromEntity(savedRent);
    }

    // 도서 대출
    @Override
    public boolean rent(Long id, Long userId) {
        Rents rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "반납 기록이 존재하지 않습니다."));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));

        // 대출 상태 확인 후 대출 처리
        if (!rentState(id)) {
            // 반납 중인 경우 대출 시간 기록
            rent.setRentDate(LocalDateTime.now());
            rent.setUser(user);
            rentRepository.save(rent); // 반납 후 상태 저장
        } else {//대출 중이 아닌 경우 예외 처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 책이 아니거나 이미 대출되었습니다.");
        }

        return true;
    }

    // 도서 반납
    @Override
    public boolean remove(Long id) {
        Rents rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "대출 기록이 존재하지 않습니다."));

        // 대출 상태 확인 후 반납 처리
        if (rentState(id)) {
            // 대출 중인 경우 반납 시간 기록
            rent.setReturnDate(LocalDateTime.now());
            rentRepository.save(rent); // 반납 후 상태 저장
        } else {//대출 중이 아닌 경우 예외 처리
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 책이 아니거나 이미 반납되었습니다.");
        }

        return true;
    }

    // 도서 대출 상태 확인
    @Override
    public boolean rentState(Long id) {
        Rents rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "대출 기록이 존재하지 않습니다."));

        if (rent.getReturnDate() != null && rent.getReturnDate().isBefore(rent.getRentDate())) { // 반납 시간이 대출 시간보다 빠르면 대출 중
            return true;
        } else { // 반납 시간이 대출 시간보다 같거나 늦으면 반납됨
            return false;
        }
    }
}

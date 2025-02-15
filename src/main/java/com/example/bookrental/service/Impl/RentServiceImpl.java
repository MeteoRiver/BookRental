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

    //대출 등록
    @Override
    public RentModel insert(Long bookId, Long userId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 책이 존재하지 않습니다."));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));

        if (rentRepository.existsByBookAndUser(book, user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 책의 대여내역을 새로 생성할 수 없습니다.");
        }

        Rents rent = Rents.builder()
                .book(book)
                .user(user)
                .RentDate(LocalDateTime.now())
                .build();

        Rents savedRent = rentRepository.save(rent);

        return RentModel.fromEntity(savedRent);
    }

    // 도서 대출
    @Override
    public boolean rent(Long bookId, Long userId) {
        // 사용자가 존재하는지 확인
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));
        // 도서 대출 기록이 있는지 확인
        Rents rent = rentRepository.findByBook_BookId(bookId)
                .orElseGet(() -> convertToEntity(insert(bookId, userId))); // 없으면 insert 실행

        // 이미 대출 중인 경우 예외 발생
        if (bookState(bookId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 도서는 이미 대출 중입니다.");
        }

        // 대출 처리
        rent.setRentDate(LocalDateTime.now());
        rent.setBook(book);
        rent.setUser(user);
        rentRepository.save(rent);

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

        if (rent.getReturnDate() == null || rent.getReturnDate().isBefore(rent.getRentDate())) { // 반납 시간이 대출 시간보다 빠르거나 반납이 없는경우 대출
            return true;
        } else { // 반납 시간이 대출 시간보다 같거나 늦으면 반납됨
            return false;
        }
    }

    // 도서 대출 상태 확인(책 번호로)
    public boolean bookState(Long bookId) {
        return rentRepository.findByBook_BookId(bookId)
                .map(rent -> rent.getReturnDate() == null) // 반납 날짜가 없으면 대출 중
                .orElse(false); // 대출 기록이 없으면 false 반환
    }

    public Rents convertToEntity(RentModel rentModel) {
        Books book = bookRepository.findById(rentModel.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 책이 존재하지 않습니다."));
        Users user = userRepository.findById(rentModel.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));

        return Rents.builder()
                .book(book)
                .user(user)
                .RentDate(rentModel.getRentDate())  // RentModel의 RentDate 사용
                .ReturnDate(rentModel.getReturnDate())  // RentModel의 ReturnDate 사용
                .build();
    }
}



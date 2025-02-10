package com.example.bookrental.service.Impl;

import com.example.bookrental.model.domain.RentModel;
import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.entity.Rents;
import com.example.bookrental.model.repository.BookRepository;
import com.example.bookrental.model.repository.RentRepository;
import com.example.bookrental.service.RentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final BookRepository bookRepository;

    public RentServiceImpl(RentRepository rentRepository, BookRepository bookRepository) {
        this.rentRepository = rentRepository;
        this.bookRepository = bookRepository;
    }

    // 도서 대출
    @Override
    public Object rent(RentModel rentModel) {
        Books book = bookRepository.findById(rentModel.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 책이 존재하지 않습니다."));

        // 이미 대출된 책인지 확인
        if (rentRepository.existsById(book.getBookId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 대출된 책입니다.");
        }

        Rents rent = Rents.builder()
                .book(book)
                .rental(true)
                .build();

        Rents savedRent = rentRepository.save(rent);
        return RentModel.fromEntity(savedRent);
    }

    // 도서 반납
    @Override
    public boolean remove(Long id) {
        return rentRepository.findById(id)
                .map(rent -> {
                    rentRepository.delete(rent);
                    return true;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "대출 기록이 존재하지 않습니다."));
    }

    // 도서 대출 상태 확인
    @Override
    public boolean rentState(Long id) {
        return rentRepository.existsById(id);
    }
}

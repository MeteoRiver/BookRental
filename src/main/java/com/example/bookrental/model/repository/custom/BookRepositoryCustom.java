package com.example.bookrental.model.repository.custom;

import com.example.bookrental.model.domain.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<BookModel> findAllBook(Pageable pageable);

    Page<BookModel> findBookById(Long id, Pageable pageable);
}
package com.example.bookrental.service;

import com.example.bookrental.model.domain.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    //도서 등록
    Object insert(BookModel bookModel);

    //도서 조회(전체)
    Page<BookModel> findAll(Pageable pageable);

    //도서 조회(Id)
    Object findById(Long bookId);

    //도서 수정
    Object update(Long id, BookModel bookModel);

    //도서 삭제
    boolean remove(Long id);

}

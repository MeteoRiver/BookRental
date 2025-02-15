package com.example.bookrental.service;

import com.example.bookrental.model.domain.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    //도서 등록
    BookModel insert(BookModel bookModel);

    //도서 조회(전체)+페이징
    Page<BookModel> findAll(Pageable pageable, List<String> tags);

    //도서 조회(Id)
    Object findById(Long bookId);

    //도서명 조회+페이징
    Page<BookModel> findByName(String title, Pageable pageable, List<String> tags);

    //저자명으로 조회+페이징
    Page<BookModel> findByAuthor(String author, Pageable pageable, List<String> tags);

    //도서 수정
    BookModel update(Long id, BookModel bookModel);

    //도서 삭제
    boolean remove(Long id);

}

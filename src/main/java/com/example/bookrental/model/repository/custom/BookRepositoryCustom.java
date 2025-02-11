package com.example.bookrental.model.repository.custom;

import com.example.bookrental.model.domain.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    //도서 목록 전체 조회(페이지)
    Page<BookModel> findAllBook(Pageable pageable);

    // 도서 제목 검색 (부분 검색 지원)
    Page<BookModel> findByName(String name, Pageable pageable);

    // 저자 이름 검색 (부분 검색 지원)
    Page<BookModel> findByAuthor(String author, Pageable pageable);

}
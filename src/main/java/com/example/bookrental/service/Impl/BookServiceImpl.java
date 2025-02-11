package com.example.bookrental.service.Impl;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.repository.BookRepository;
import com.example.bookrental.service.BookService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //도서 등록
    @Override
    public Object insert(BookModel bookModel){
        Books savedBook = bookRepository.save(Books.builder()
                        .bookName(bookModel.getBookName())
                        .author(bookModel.getAuthor())
                        .PubDate(bookModel.getPubDate())
                        .build());

        return BookModel.fromEntity(savedBook);
    }

    //도서 조회(전체)
    public Page<BookModel> findAll(Pageable pageable){
        return bookRepository.findAllBook(pageable);
    }

    // 도서 조회 (단일 조회)
    public Object findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "책이 존재하지 않습니다."));
    }

    // 도서 제목 검색 (부분 검색 지원)
    public Page<BookModel> findByName(String title, Pageable pageable) {
        return bookRepository.findByName(title, pageable);
    }

    // 저자 이름 검색 (부분 검색 지원)
    public Page<BookModel> findByAuthor(String author, Pageable pageable) {
        return bookRepository.findByAuthor(author, pageable);
    }


    //도서 수정
    public Object update(Long id, BookModel bookModel){
        return bookRepository.findById(id)
                .map(existBook ->{
                    existBook.setBookName(bookModel.getBookName());
                    return BookModel.fromEntity(bookRepository.save(existBook));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "책이 존재하지 않습니다."));
    }

    //도서 삭제
    public boolean remove(Long id){
        try {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
                return !bookRepository.existsById(id);
            }
            return false;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }


}

package com.example.bookrental.service.Impl;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.entity.Tags;
import com.example.bookrental.model.repository.BookRepository;
import com.example.bookrental.model.repository.TagRepository;
import com.example.bookrental.service.BookService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository; // 태그 추가를 위해 필요

    public BookServiceImpl(BookRepository bookRepository, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
    }

    // 도서 등록 (태그 포함)
    @Override
    @CacheEvict(value = "books", allEntries = true)
    public BookModel insert(BookModel bookModel) {
        Set<Tags> tagSet = new HashSet<>();
        if (bookModel.getTagName() != null && !bookModel.getTagName().isEmpty()) {
            tagSet = bookModel.getTagName().stream()
                    .map(this::addTagIfNotExists) // 태그가 없으면 새로 추가
                    .collect(Collectors.toSet());
        }

        Books savedBook = bookRepository.save(Books.builder()
                .bookName(bookModel.getBookName())
                .author(bookModel.getAuthor())
                .pubDate(bookModel.getPubDate())
                .tags(tagSet)
                .build());

        return BookModel.fromEntity(savedBook);
    }

    // 전체 도서 조회 (태그 필터링 추가)
    @Cacheable(value = "books", key = "#title + (#pageable != null ? #pageable.toString() : 'empty') + (#tags != null ? #tags.toString() : 'empty')")
    public Page<BookModel> findAll(Pageable pageable, List<String> tags) {
        return bookRepository.findAllBook(pageable, tags);
    }

    // 도서 단일 조회 (BookModel로 반환)
    @Cacheable(value = "books", key = "#bookId")
    public BookModel findById(Long bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "책이 존재하지 않습니다."));
        return BookModel.fromEntity(book);
    }

    // 제목 검색 (태그 필터링 추가)
    @Cacheable(value = "books", key = "#title + (#pageable != null ? #pageable.toString() : 'empty') + (#tags != null ? #tags.toString() : 'empty')")
    public Page<BookModel> findByName(String title, Pageable pageable, List<String> tags) {
        return bookRepository.findByName(title, pageable, tags);
    }

    // 저자 검색 (태그 필터링 추가)
    @Cacheable(value = "books", key = "#author + (#pageable != null ? #pageable.toString() : 'empty') + (#tags != null ? #tags.toString() : 'empty')")
    public Page<BookModel> findByAuthor(String author, Pageable pageable, List<String> tags) {
        return bookRepository.findByAuthor(author, pageable, tags);
    }

    // 도서 수정 (태그 포함)
    @CacheEvict(value = "books", allEntries = true)
    public BookModel update(Long id, BookModel bookModel) {
        Books updatedBook = bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setBookName(bookModel.getBookName());
                    existingBook.setAuthor(bookModel.getAuthor());  // ✅ 저자 정보 수정 반영
                    existingBook.setPubDate(bookModel.getPubDate()); // ✅ 출판일 수정 반영

                    // ✅ 태그 수정 처리
                    if (bookModel.getTagName() != null) {
                        Set<Tags> updatedTags = bookModel.getTagName().stream()
                                .map(this::addTagIfNotExists)  // addTagIfNotExists 메서드 사용
                                .collect(Collectors.toSet());
                        existingBook.setTags(updatedTags);
                    }

                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "책이 존재하지 않습니다."));

        return BookModel.fromEntity(updatedBook);
    }

    // 도서 삭제
    @CacheEvict(value = "books", allEntries = true)
    public boolean remove(Long id) {
        try {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
                return !bookRepository.existsById(id);
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    // 특정 태그가 없으면 새로 등록하거나 기존 태그 반환
    private Tags addTagIfNotExists(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tags(null, tagName, new HashSet<>())));
    }
}
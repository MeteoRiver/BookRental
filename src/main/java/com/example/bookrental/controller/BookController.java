package com.example.bookrental.controller;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.service.BookService;
import com.example.bookrental.service.Impl.RentServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService, RentServiceImpl rentService) {
        this.bookService = bookService;
    }

    // 책 추가 (대여 등록 포함)
    @PostMapping
    @Transactional
    public ResponseEntity<?> insert(@RequestBody BookModel bookModel) {
        return ResponseEntity.ok(bookService.insert(bookModel));
    }

    // 책 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BookModel bookModel) {
        return ResponseEntity.ok(bookService.update(id, bookModel));
    }

    // 책 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.remove(id));
    }

    // 책 전체 조회 (페이징 + 정렬 + 태그 필터링 지원)
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) List<String> tags) {
        System.out.println(" 책 조회 요청 들어옴! page=" + page + ", size=" + size);

        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return ResponseEntity.ok(bookService.findAll(PageRequest.of(page, size, sort), tags));
    }

    // 책 아이디로 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // 책 제목 검색 (태그 필터링 지원)
    @GetMapping("/search/title")
    public ResponseEntity<?> searchByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> tags) {
        return ResponseEntity.ok(bookService.findByName(title, PageRequest.of(page, size), tags));
    }

    // 저자 검색 (태그 필터링 지원)
    @GetMapping("/search/author")
    public ResponseEntity<?> searchByAuthor(
            @RequestParam String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> tags) {
        return ResponseEntity.ok(bookService.findByAuthor(author, PageRequest.of(page, size), tags));
    }
}
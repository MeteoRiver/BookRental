package com.example.bookrental.controller;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.repository.BookRepository;
import com.example.bookrental.service.Impl.BookServiceImpl;
import com.example.bookrental.service.Impl.RentServiceImpl;
import io.swagger.annotations.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@Tag(name = "01 .Book")
@RequestMapping("/api/book")
public class BookController {

    private final BookServiceImpl bookService;
    private final RentServiceImpl rentService;

    public BookController(BookServiceImpl bookServiceImpl, RentServiceImpl rentService) {
        this.bookService = bookServiceImpl;
        this.rentService = rentService;
    }

    //책 추가
    @PostMapping
    //@Operation(summary = "책 추가", description = "입력된 책을 저장합니다.", tags = {"01. Book"})
    public ResponseEntity<?> insert(@RequestBody BookModel bookModel) {
        rentService.insert(bookModel.getBookId());
        return ResponseEntity.ok(bookService.insert(bookModel));
    }

    //책 수정
    @PutMapping("/{id}")
    //@Operation(summary = "책 수정", description = "입력된 책을 수정합니다.", tags = {"01. Book"})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BookModel bookModel) {
        return ResponseEntity.ok(bookService.update(id, bookModel));
    }

    //책 삭제
    @DeleteMapping("/{id}")
    //@Operation(summary = "책 삭제", description = "입력된 책을 삭제합니다.", tags = {"01. Book"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.remove(id));
    }

    // 책 전체 조회 (페이징 + 정렬 지원)
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "bookName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return ResponseEntity.ok(bookService.findAll(PageRequest.of(page, size, sort)));
    }

    // 책 아이디로 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // 책 제목 검색
    @GetMapping("/search/title")
    public ResponseEntity<?> searchByTitle(
            @RequestParam String title,
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(bookService.findByName(title, PageRequest.of(page, size)));
    }

    // 저자 검색
    @GetMapping("/search/author")
    public ResponseEntity<?> searchByAuthor(
            @RequestParam String author,
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(bookService.findByAuthor(author, PageRequest.of(page, size)));
    }
}

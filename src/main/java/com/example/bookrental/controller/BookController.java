package com.example.bookrental.controller;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.service.Impl.BookServiceImpl;
import io.swagger.annotations.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@Tag(name = "01 .Book")
@RequestMapping("/api/book")
public class BookController {

    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookServiceImpl) {this.bookService = bookServiceImpl;}

    //책 추가
    @PostMapping
    //@Operation(summary = "책 추가", description = "입력된 책을 저장합니다.", tags = {"01. Book"})
    public ResponseEntity<?> insert(@RequestBody BookModel bookModel) {
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

    //책 전체조회
    @GetMapping
    //@Operation(summary = "책 전체 조회", description = "모든 책을 검섹합니다.", tags = {"01. Book"})
    public ResponseEntity<?> findAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bookService.findAll(PageRequest.of(page, size)));
    }

    //책 아이디로 조회
    @GetMapping("/{id}")
    //@Operation(summary = "책 상세 조회", description = "입력된 책을 검색합니다.", tags = {"01. Book"})
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }
}

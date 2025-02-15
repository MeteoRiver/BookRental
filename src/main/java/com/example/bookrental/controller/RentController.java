package com.example.bookrental.controller;

import com.example.bookrental.service.Impl.RentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    private final RentServiceImpl rentService;

    public RentController(RentServiceImpl rentService) {
        this.rentService = rentService;
    }

    // 1. 도서 대출 등록
    @PostMapping("/{bookId}/{userId}")
    public ResponseEntity<?> insert(@PathVariable Long bookId, @PathVariable Long userId) {
        return ResponseEntity.ok(rentService.insert(bookId,userId));
    }

    // 2. 대출 상태 확인
    @GetMapping("/{id}")
    public ResponseEntity<?> checkRentStatus(@PathVariable Long id) {
        boolean rentState = rentService.rentState(id);
        String status = rentState ? "rent" : "return";
        return ResponseEntity.ok(status);
    }
    // 3. 도서 대출
    @PutMapping("/{bookId}/{userId}")
    public ResponseEntity<?> rentBook(@PathVariable Long bookId, @PathVariable Long userId) {
        return rentService.rent(bookId, userId)
                ? ResponseEntity.ok("rent")
                : ResponseEntity.badRequest().body("failed");
    }

    // 4. 도서 반납
    @PutMapping("/return/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        return rentService.remove(id)
                ? ResponseEntity.ok("return")
                : ResponseEntity.badRequest().body("failed");
    }
}

package com.example.bookrental.controller;

import com.example.bookrental.model.domain.RentModel;
import com.example.bookrental.service.Impl.BookServiceImpl;
import com.example.bookrental.service.Impl.RentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.mappers.ModelMapper;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    private final RentServiceImpl rentService;

    public RentController(RentServiceImpl rentService) {
        this.rentService = rentService;
    }

    // 1. 도서 대출 등록
    @PostMapping
    public ResponseEntity<?> rentBook(@RequestBody RentModel rentModel) {
        return ResponseEntity.ok(rentService.rent(rentModel));
    }

    // 2. 대출 상태 확인
    @GetMapping("/{id}")
    public ResponseEntity<?> checkRentStatus(@PathVariable Long id) {
        return ResponseEntity.ok(rentService.rentState(id));
    }

    // 3. 도서 반납
    @DeleteMapping("/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        return rentService.remove(id)
                ? ResponseEntity.ok("반납 완료")
                : ResponseEntity.badRequest().body("반납 실패");
    }
}

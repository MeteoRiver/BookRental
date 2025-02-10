package com.example.bookrental.controller;

import com.example.bookrental.model.domain.UserModel;
import com.example.bookrental.service.Impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // 1. 사용자 등록
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserModel userModel) {
        return ResponseEntity.ok(userService.insert(userModel));
    }

    // 2. 사용자 전체 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    // 3. 특정 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findAllByUserId(id));
    }
}

package com.example.bookrental.model.repository;

import com.example.bookrental.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // 이메일 중복 체크
    boolean existsByEmail(String email);
}

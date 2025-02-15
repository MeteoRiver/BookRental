package com.example.bookrental.model.repository;

import com.example.bookrental.model.entity.Tags;
import com.example.bookrental.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tags, Long> {
    Optional<Tags> findByName(String tagName);
}

package com.example.bookrental.model.repository;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.repository.custom.BookRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Books, Long>, BookRepositoryCustom {

}
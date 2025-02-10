package com.example.bookrental.model.repository;

import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.repository.custom.BookRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Long>, BookRepositoryCustom {

}
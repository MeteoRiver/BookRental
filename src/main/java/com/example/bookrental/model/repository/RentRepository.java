package com.example.bookrental.model.repository;

import com.example.bookrental.model.entity.Books;
import com.example.bookrental.model.entity.Rents;
import com.example.bookrental.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<Rents, Long> {

    Optional<Rents> findByBook_BookId(Long bookId);

    boolean existsByBookAndUser(Books book, Users user);

}


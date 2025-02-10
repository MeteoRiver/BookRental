package com.example.bookrental.model.repository;

import com.example.bookrental.model.entity.Rents;
import com.example.bookrental.model.repository.custom.RentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepository extends JpaRepository<Rents, Long>, RentRepositoryCustom {


}


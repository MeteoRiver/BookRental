package com.example.bookrental.model.repository.custom;

import com.example.bookrental.model.domain.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<UserModel> findAll(Pageable pageable);

}

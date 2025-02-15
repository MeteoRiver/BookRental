package com.example.bookrental.service;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.domain.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    //사용자 등록
    Object insert(UserModel userModel);

    //사용자 전체 조회
    List<UserModel> findAll();

    //사용자 아이디로 조회
    Object findAllByUserId(Long userId);

}

package com.example.bookrental.service.Impl;

import com.example.bookrental.model.domain.BookModel;
import com.example.bookrental.model.domain.UserModel;
import com.example.bookrental.model.entity.Users;
import com.example.bookrental.model.repository.UserRepository;
import com.example.bookrental.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 등록
    @Override
    public Object insert(UserModel userModel) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(userModel.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.");
        }

        Users savedUser = userRepository.save(Users.builder()
                .userName(userModel.getUserName())
                .password(userModel.getPassword())  // 실제 서비스에서는 비밀번호 암호화 필요
                .email(userModel.getEmail())
                .phone(userModel.getPhone())
                .build());

        return UserModel.fromEntity(savedUser);
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserModel::fromEntity);
    }

    @Override
    public UserModel findAllByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(UserModel::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));
    }
}

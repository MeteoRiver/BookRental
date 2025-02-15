package com.example.bookrental.service.Impl;

import com.example.bookrental.model.domain.UserModel;
import com.example.bookrental.model.entity.Users;
import com.example.bookrental.model.repository.UserRepository;
import com.example.bookrental.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository , BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    // 사용자 등록
    @Override
    public Object insert(UserModel userModel) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(userModel.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.");
        }
    String encodedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
        Users savedUser = userRepository.save(Users.builder()
                .userName(userModel.getUserName())
                .password(encodedPassword)
                .email(userModel.getEmail())
                .phone(userModel.getPhone())
                .build());

        return UserModel.fromEntity(savedUser);
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll().stream()
                .map(UserModel::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserModel findAllByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(UserModel::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));
    }
}

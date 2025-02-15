package com.example.bookrental.service;

import com.example.bookrental.model.entity.Users;
import com.example.bookrental.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername 호출됨: {}", username);

        Users user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("사용자 정보를 찾을 수 없음: {}", username);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        logger.info("사용자 정보 발견: {}", username);
        return new CustomUserDetails(user);
    }
}

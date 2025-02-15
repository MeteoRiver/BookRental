package com.example.bookrental.Filter;

import com.example.bookrental.service.CustomUserDetails;
import com.example.bookrental.service.LoginModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    //인증
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication 메서드 호출됨");
        LoginModel loginModel = new LoginModel();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginModel = objectMapper.readValue(messageBody, LoginModel.class);
            log.info("로그인 요청 데이터: username={}, password={}", loginModel.getUsername(), loginModel.getPassword());
        } catch (Exception e) {
            log.error("로그인 요청 파싱 중 오류 발생", e);
            throw new RuntimeException(e);
        }

        // UsernamePasswordAuthenticationToken 생성 시 권한 설정
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginModel.getUsername(), loginModel.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        log.info("인증 토큰 생성: {}", authToken);
        return authenticationManager.authenticate(authToken);
    }

    //인증 성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        log.info("successfulAuthentication 메서드 호출됨 - 로그인 성공");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = userDetails.getUsername();
        Long id = 0L;

        if (userDetails instanceof CustomUserDetails) {
            id = ((CustomUserDetails) userDetails).getUserId();
            log.info("사용자 인증 성공: username={}, id={}", username, id);
        }

        // 인증 후 권한 부여 (권한은 "ROLE_USER"로 설정됨)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? "ROLE_USER" : authorities.iterator().next().getAuthority();
        log.info("사용자 역할: {}", role);

        String access = jwtUtil.createJwt("access", id, username, 600000L);
        String refresh = jwtUtil.createJwt("refresh", id, username, 86400000L);
        log.info("JWT 생성 완료 - Access Token={}, Refresh Token={}", access, refresh);

        response.setHeader("access", access);
        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    //실패시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        log.info("unsuccessfulAuthentication 메서드 호출됨 - 로그인 실패");
        log.warn("로그인 실패 원인: {}", failed.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        log.info("쿠키 생성 - Key={}, Value={}", key, value);
        return cookie;
    }
}
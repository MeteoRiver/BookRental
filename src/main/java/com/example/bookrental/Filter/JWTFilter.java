package com.example.bookrental.Filter;

import com.example.bookrental.model.entity.Users;
import com.example.bookrental.service.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("🔹 [JWTFilter] 요청 URL: {}", request.getRequestURI());

        // 쿠키 또는 헤더에서 토큰 확인
        String token = getTokenFromRequest(request);
        log.info("🔹 [JWTFilter] 추출된 토큰: {}", token);

        // 토큰이 없으면 다음 필터로 넘김
        if (token == null) {
            log.warn("⚠️ [JWTFilter] 토큰이 존재하지 않음");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 만료 여부 확인
            if (jwtUtil.isExpired(token)) {
                log.warn("⚠️ [JWTFilter] 토큰 만료됨");
                sendErrorResponse(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // access 토큰인지 확인
            String category = jwtUtil.getCategory(token);
            log.info("🔹 [JWTFilter] 토큰 카테고리: {}", category);

            if (!"access".equals(category)) {
                log.warn("⚠️ [JWTFilter] Invalid access token");
                sendErrorResponse(response, "Invalid access token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 토큰에서 id, username 추출
            Long id = jwtUtil.getId(token);
            String username = jwtUtil.getUserName(token);
            log.info("✅ [JWTFilter] 토큰 검증 성공 - ID: {}, Username: {}", id, username);

            // 인증 객체 생성 후 SecurityContext에 저장
            Authentication authToken = createAuthentication(id, username);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("✅ [JWTFilter] SecurityContext에 인증 정보 저장됨: {}", authToken);

        } catch (ExpiredJwtException e) {
            log.warn("⚠️ [JWTFilter] 토큰이 만료됨 (예외 발생)");
            sendErrorResponse(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // 쿠키 및 헤더에서 토큰을 추출하는 메소드
    private String getTokenFromRequest(HttpServletRequest request) {
        // 헤더에서 토큰 확인
        String accessToken = request.getHeader("Authorization");
        log.info("🔹 [JWTFilter] Authorization 헤더에서 추출한 토큰: {}", accessToken);

        if (accessToken != null) {
            return accessToken;
        }

        // 쿠키에서 토큰 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("🔹 [JWTFilter] 쿠키 확인 - {}: {}", cookie.getName(), cookie.getValue());
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        log.warn("⚠️ [JWTFilter] 요청에서 토큰을 찾을 수 없음");
        return null;
    }

    // CustomUserDetails를 생성하고 인증 객체를 생성하는 메소드
    private Authentication createAuthentication(Long id, String username) {
        log.info("🔹 [JWTFilter] 인증 객체 생성 - ID: {}, Username: {}", id, username);

        Users user = new Users();
        user.setUserId(id);
        user.setUserName(username);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        log.info("✅ [JWTFilter] 인증 객체 생성 완료: {}", auth);
        return auth;
    }

    // 에러 응답을 처리하는 메소드
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.print(message);
    }
}

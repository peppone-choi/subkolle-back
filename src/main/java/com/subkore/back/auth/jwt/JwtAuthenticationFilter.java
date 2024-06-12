package com.subkore.back.auth.jwt;

import com.subkore.back.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 토큰 가져오기
        String accessToken = resolveToken((HttpServletRequest) request);
        // 토큰 유효성 검사
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            // 토큰에서 Authentication 객체 가져오기
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            // SecurityContext에 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보를 가져오는 메서드
    private String resolveToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) {
            return null;
        }
        return request.getHeader("Authorization").substring(7);
    }
}

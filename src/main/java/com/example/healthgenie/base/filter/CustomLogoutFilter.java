package com.example.healthgenie.base.filter;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.example.healthgenie.base.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = CookieUtils.getCookie(request, "refresh").getValue();

        if (!StringUtils.hasText(refresh)) {
            throw new CustomException(JWT_ERROR, "refresh 쿠키가 없습니다.");
        }

        try {
            jwtUtils.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(JWT_ERROR, "만료된 토큰입니다.");
        }

        String category = jwtUtils.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new CustomException(NOT_VALID, "category="+category);
        }

        if (!refreshTokenRepository.existsByRefreshToken(refresh)) {
            throw new CustomException(DATA_NOT_FOUND, "refresh="+refresh);
        }

        refreshTokenRepository.deleteByRefreshToken(refresh);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}

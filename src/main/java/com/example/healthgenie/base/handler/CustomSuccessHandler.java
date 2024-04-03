package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import static com.example.healthgenie.base.constant.Constants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String email = user.getEmail();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String access = jwtUtils.createJwt("access", email, role, ACCESS_TOKEN_EXPIRATION_MS);
        String refresh = jwtUtils.createJwt("refresh", email, role, REFRESH_TOKEN_EXPIRATION_MS);
        log.info("[인증 성공]");
        log.info("[ACCESS TOKEN]={}", access);
        log.info("[REFRESH TOKEN]={}", refresh);

        saveRefreshToken(refresh, email, REFRESH_TOKEN_EXPIRATION_MS);

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.addCookie(CookieUtils.createCookie("access", access, "localhost"));
        response.addCookie(CookieUtils.createCookie("refresh", refresh, "localhost"));
        response.sendRedirect("http://localhost:3000/login-success");
    }

    private void saveRefreshToken(String refresh, String email, Long expirationMs) {
        RefreshToken refreshTokenObj = RefreshToken.builder()
                .refreshToken(refresh)
                .keyEmail(email)
                .expiration(new Date(System.currentTimeMillis() + expirationMs).toString())
                .build();

        refreshTokenRepository.save(refreshTokenObj);
    }
}

package com.example.healthgenie.boundedContext.refreshtoken.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.refreshtoken.dto.TokenResponse;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.example.healthgenie.base.constant.Constants.ACCESS_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.base.constant.Constants.REFRESH_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.base.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public RefreshToken save(String refreshToken, String email, Long expirationMs) {
        RefreshToken refreshTokenObj = RefreshToken.builder()
                .refreshToken(refreshToken)
                .keyEmail(email)
                .expiration(LocalDateTime.now().plusSeconds(expirationMs/1000))
                .build();

        return refreshTokenRepository.save(refreshTokenObj);
    }

    @Transactional
    public TokenResponse reissue(String refresh) {
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

        String email = jwtUtils.getEmail(refresh);
        String role = jwtUtils.getRole(refresh);

        String newAccess = jwtUtils.createJwt("access", email, role, ACCESS_TOKEN_EXPIRATION_MS);
        String newRefresh = jwtUtils.createJwt("refresh", email, role, REFRESH_TOKEN_EXPIRATION_MS);

        refreshTokenRepository.deleteByRefreshToken(refresh);
        save(newRefresh, email, REFRESH_TOKEN_EXPIRATION_MS);

        return TokenResponse.builder()
                .access(newAccess)
                .refresh(newRefresh)
                .build();
    }
}
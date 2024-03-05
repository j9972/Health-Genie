package com.example.healthgenie.boundedContext.refreshtoken.service;

import com.example.healthgenie.base.exception.JwtException;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.base.exception.JwtErrorResult.NOT_FOUND_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken save(String refreshToken, String email) {
        RefreshToken refreshTokenObj = RefreshToken.builder()
                .keyEmail(email)
                .refreshToken(refreshToken)
                .build();

        return refreshTokenRepository.save(refreshTokenObj);
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new JwtException(NOT_FOUND_TOKEN));
    }

    @Transactional
    public void deleteByKeyEmail(String email) {
        refreshTokenRepository.deleteByKeyEmail(email);
    }
}
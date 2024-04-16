package com.example.healthgenie.boundedContext.refreshtoken.repository;

import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Boolean existsByRefreshToken(String refreshToken);
    @Transactional
    void deleteByRefreshToken(String refreshToken);
    @Transactional
    void deleteAllByExpirationBefore(LocalDateTime now);
}
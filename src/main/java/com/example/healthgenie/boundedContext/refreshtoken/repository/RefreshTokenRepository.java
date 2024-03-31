package com.example.healthgenie.boundedContext.refreshtoken.repository;

import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByKeyEmail(String email);
    Boolean existsByKeyEmail(String email);
    Boolean existsByRefreshToken(String refreshToken);
    void deleteByKeyEmail(String email);
    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
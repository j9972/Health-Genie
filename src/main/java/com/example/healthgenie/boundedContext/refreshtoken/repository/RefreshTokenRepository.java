package com.example.healthgenie.boundedContext.refreshtoken.repository;

import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    boolean existsByKeyEmail(String email);
    void deleteByKeyEmail(String email);
}
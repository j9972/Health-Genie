package com.example.healthgenie.repository;

import com.example.healthgenie.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    // Optional<RefreshToken> findByRefreshToken(String refreshToken);
}


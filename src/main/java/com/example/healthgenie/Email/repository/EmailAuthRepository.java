package com.example.healthgenie.Email.repository;

import com.example.healthgenie.Email.entity.EmailAuthCode;
import com.example.healthgenie.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuthCode, Long> {
    Optional<EmailAuthCode> findByEmail(String email);
}

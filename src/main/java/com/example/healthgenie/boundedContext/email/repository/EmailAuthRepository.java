package com.example.healthgenie.boundedContext.email.repository;

import com.example.healthgenie.boundedContext.email.entity.EmailAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuthCode, Long> {
    Optional<EmailAuthCode> findByEmail(String email);
}

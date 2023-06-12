package com.example.healthgenie.repository;

import com.example.healthgenie.entity.EmailAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthCodeRepository extends JpaRepository<EmailAuthCode,Long> {

    public Optional<EmailAuthCode> findByCode(String code);
}

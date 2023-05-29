package com.example.healthgenie.repository;

import com.example.healthgenie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // user_tb findByEmailId(@Param("email") String email);
}

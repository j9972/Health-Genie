package com.example.healthgenie.repository;

import com.example.healthgenie.entity.Role;
import com.example.healthgenie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // user_tb findByEmailId(@Param("email") String email);

    Optional<User> findByEmailAndProvider(String email,String provider);
    User findByRoleAndId(Role role, Long Id);
}

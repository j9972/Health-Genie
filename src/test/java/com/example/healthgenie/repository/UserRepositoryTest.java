package com.example.healthgenie.repository;

import com.example.healthgenie.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Repository
@Slf4j
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test

}
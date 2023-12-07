package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.UserRegisterDto;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("정상적인 회원가입")
    void signUp() {
        UserRegisterDto request = UserRegisterDto.builder()
                .name("test1")
                .role(Role.EMPTY)
                .level(Level.EMPTY)
                .authProvider(AuthProvider.EMPTY)
                .email("test1@test.com")
                .uniName("테스트대학교")
                .build();

        User savedUser = userService.signUp(request);

        assertThat(savedUser.getEmail()).isEqualTo("test1@test.com");
    }

    @Test
    @DisplayName("정상적인 Role 변경")
    void updateRole() {
        User user = testKrUtils.createUser("test1", Role.EMPTY, "test1@test.com");

        testKrUtils.login(user);

        userService.updateRole(Role.TRAINER);
    }
}
package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.UserRegisterDto;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import jakarta.persistence.EntityManager;
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

    @Autowired
    EntityManager em;

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

        em.flush();
        em.clear();

        assertThat(user.getRole().getCode()).isEqualTo("TRAINER");
    }

    @Test
    @DisplayName("정상적인 닉네임 변경")
    void updateNickname() {
        User user = testKrUtils.createUser("test1", Role.EMPTY, "test1@test.com");

        testKrUtils.login(user);

        userService.updateNickname("변경된 닉네임");

        em.flush();
        em.clear();

        assertThat(user.getNickname()).isEqualTo("변경된 닉네임");
    }
}
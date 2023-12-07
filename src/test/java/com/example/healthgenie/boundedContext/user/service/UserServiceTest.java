package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;

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

    User default1;
    User user1;
    User trainer1;

    @BeforeEach
    void before() {
        default1 = testKrUtils.createUser("default1", Role.EMPTY, "default1@test.com");
        user1 = testKrUtils.createUser("user1", Role.USER, "user1@test.com");
        trainer1 = testKrUtils.createUser("trainer1", Role.TRAINER, "trainer1@test.com");
    }

    @Test
    @DisplayName("정상적인 회원가입")
    void signUp() {
        // given
        UserRequest request = UserRequest.builder()
                .email("test1@test.com")
                .name("test1")
                .authProvider(AuthProvider.EMPTY)
                .build();

        // when
        UserResponse response =
                userService.signUp(request.getEmail(), request.getName(), request.getAuthProvider());

        // then
        assertThat(response.getEmail()).isEqualTo("test1@test.com");
        assertThat(response.getName()).isEqualTo("test1");
        assertThat(response.getAuthProvider()).isEqualTo(AuthProvider.EMPTY);
    }

    @Test
    @DisplayName("정상적인 Role 변경")
    void updateRole() {
        // given
        testKrUtils.login(default1);

        // when
        UserResponse response = userService.updateRole(default1.getId(), Role.USER);

        // then
        assertThat(response.getId()).isEqualTo(default1.getId());
        assertThat(default1.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("정상적인 닉네임 변경")
    void updateNickname() {
        // given
        testKrUtils.login(default1);

        // when
        UserResponse response = userService.updateNickname(default1.getId(), "변경된 닉네임");

        // then
        assertThat(response.getId()).isEqualTo(default1.getId());
        assertThat(default1.getNickname()).isEqualTo("변경된 닉네임");
    }
}
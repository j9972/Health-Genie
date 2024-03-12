package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Gender;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        default1 = testKrUtils.createUser("default1@test.com", "default1", AuthProvider.EMPTY, Role.EMPTY);
        user1 = testKrUtils.createUser("user1@test.com", "user1", AuthProvider.KAKAO, Role.USER);
        trainer1 = testKrUtils.createUser("trainer1@test.com", "trainer1", AuthProvider.GOOGLE, Role.TRAINER);
    }

    @Test
    @DisplayName("회원 가입")
    void signUp() {
        // given

        // when
        User saveUser = userService.signUp("test1@test.com", "test1", AuthProvider.KAKAO);

        // then
        assertThat(saveUser.getEmail()).isEqualTo("test1@test.com");
        assertThat(saveUser.getName()).isEqualTo("test1");
        assertThat(saveUser.getAuthProvider()).isEqualTo(AuthProvider.KAKAO);
        assertThat(saveUser.getRole()).isEqualTo(Role.EMPTY);
    }

    @Test
    @DisplayName("회원 단건 조회 - id")
    void findById() {
        // given

        // when
        User findUser = userService.findById(user1.getId());

        // then
        assertThat(findUser.getId()).isEqualTo(user1.getId());
        assertThat(findUser.getName()).isEqualTo(user1.getName());
        assertThat(findUser.getAuthProvider()).isEqualTo(user1.getAuthProvider());
        assertThat(findUser.getRole()).isEqualTo(user1.getRole());
    }

    @Test
    @DisplayName("회원 단건 조회 - email")
    void findByEmail() {
        // given

        // when
        User findUser = userService.findByEmail(trainer1.getEmail());

        // then
        assertThat(findUser.getId()).isEqualTo(trainer1.getId());
        assertThat(findUser.getName()).isEqualTo(trainer1.getName());
        assertThat(findUser.getAuthProvider()).isEqualTo(trainer1.getAuthProvider());
        assertThat(findUser.getRole()).isEqualTo(trainer1.getRole());
    }

    @Test
    @DisplayName("회원 정보 변경 - Role")
    void updateRole() {
        // given

        // when
        User updateUser = userService.update(default1, Role.USER);

        // then
        assertThat(updateUser.getId()).isEqualTo(default1.getId());
        assertThat(updateUser.getRole()).isEqualTo(default1.getRole());
    }

    @Test
    @DisplayName("회원 정보 변경 - Level")
    void updateLevel() {
        // given

        // when
        User updateUser = userService.update(default1, Level.EXPERT);

        // then
        assertThat(updateUser.getId()).isEqualTo(default1.getId());
        assertThat(updateUser.getLevel()).isEqualTo(default1.getLevel());
    }

    @Test
    @DisplayName("회원 정보 변경 - Info")
    void updateInfo() {
        // given

        // when
        LocalDateTime birth = LocalDateTime.of(1996, 6, 15, 0, 0, 0);

        User updateUser = userService.update(default1,
                null, "변경된닉네임", Gender.MALE, birth, 180.5, 80.5, 30.5);

        // then
        assertThat(updateUser.getId()).isEqualTo(default1.getId());
        assertThat(updateUser.getNickname()).isEqualTo("변경된닉네임");
        assertThat(updateUser.getGender()).isEqualTo(Gender.MALE);
        assertThat(updateUser.getBirth()).isEqualTo(birth);
        assertThat(updateUser.getHeight()).isEqualTo(180.5);
        assertThat(updateUser.getWeight()).isEqualTo(80.5);
        assertThat(updateUser.getMuscleWeight()).isEqualTo(30.5);
    }

}
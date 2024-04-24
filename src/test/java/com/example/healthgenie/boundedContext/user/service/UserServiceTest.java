package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.DietResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("이미 가입된 이메일인 경우 회원 가입 불가능")
    void signUp_duplicateEmail_exception() {
        // given
        String duplicateEmail = "user1@test.com";

        // when

        // then
        assertThatThrownBy(() -> userService.signUp(duplicateEmail, "dupUser", AuthProvider.KAKAO, Role.USER))
                .isInstanceOf(CustomException.class);
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
    @DisplayName("존재하지 않는 회원 조회 불가능 - id")
    void findById_notExistsUser_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(CustomException.class);
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
    @DisplayName("존재하지 않는 회원 조회 불가능 - email")
    void findByEmail_notExistsUser_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> userService.findByEmail("notExists@test.com"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("회원 정보 변경 - Role")
    void update_role() {
        // given

        // when
        User updateUser = userService.update(default1, Role.USER);

        // then
        assertThat(updateUser.getId()).isEqualTo(default1.getId());
        assertThat(updateUser.getRole()).isEqualTo(default1.getRole());
    }

    @Test
    @DisplayName("유효하지 않은 Role 변경 불가능")
    void update_notValidRole_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> userService.update(user1, (Role) null))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("회원 정보 변경 - Level")
    void update_level() {
        // given

        // when
        User updateUser = userService.update(default1, Level.EXPERT);

        // then
        assertThat(updateUser.getId()).isEqualTo(default1.getId());
        assertThat(updateUser.getLevel()).isEqualTo(default1.getLevel());
    }

    @Test
    @DisplayName("유효하지 않은 Level 변경 불가능")
    void update_notValidLevel_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> userService.update(user1, (Level) null))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("회원 정보 변경 - Info")
    void update_info() {
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

    @Test
    @DisplayName("중복된 닉네임으로 변경 불가능")
    void update_duplicateNickname_exception() {
        // given
        userService.update(user1, null, "myname", null, null, null, null, null);

        // when

        // then
        assertThatThrownBy(() -> userService.update(default1, null, "myname", null, null, null, null, null))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("칼로리 계산기")
    void calculate() {
        // given
        LocalDateTime birth = LocalDateTime.of(1996, 6, 15, 0, 0, 0);

        User updateUser = userService.update(default1,
                null, "변경된닉네임", Gender.MALE, birth, 180.5, 80.5, 30.5);

        // when
        DietResponse response = userService.calculate(updateUser, 1);

        // then
        assertThat(response.getActiveRate()).isNotZero();
        assertThat(response.getBasicRate()).isNotZero();
    }

    @Test
    @DisplayName("유효하지 않은 타입인 경우 칼로리 계산 불가능")
    void calculate_notValidType_exception() {
        // given
        LocalDateTime birth = LocalDateTime.of(1996, 6, 15, 0, 0, 0);

        User updateUser = userService.update(default1,
                null, "변경된닉네임", Gender.MALE, birth, 180.5, 80.5, 30.5);

        // when

        // then
        assertThatThrownBy(() -> userService.calculate(updateUser, null))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("성별이 설정되지 않은 경우 칼로리 계산 불가능")
    void calculate_notValidGender_exception() {
        // given
        LocalDateTime birth = LocalDateTime.of(1996, 6, 15, 0, 0, 0);

        User updateUser = userService.update(default1,
                null, "변경된닉네임", null, birth, 180.5, 80.5, 30.5);

        // when

        // then
        assertThatThrownBy(() -> userService.calculate(updateUser, 1))
                .isInstanceOf(CustomException.class);
    }
}
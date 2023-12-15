package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Gender;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    @DisplayName("정상적인 프로필 변경")
    void edit() throws IOException {
        // given
        testKrUtils.login(default1);

        Path imagePath = Paths.get("images/test_code_ex.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);
        MultipartFile profilePhoto = new MockMultipartFile("profilePhoto", "test_code_ex.png", "image/png", imageBytes);

        UserRequest request = UserRequest.builder()
                .email("change@test.com")
                .uniName("변경된 대학교")
                .name("변경된 이름")
                .nickname("변경된 닉네임")
                .authProvider(AuthProvider.KAKAO)
                .role(Role.ADMIN)
                .profilePhoto(profilePhoto)
                .emailVerify(true)
                .level(Level.EXPERT)
                .height(179.9)
                .birth("9999.12.31")
                .weight(99.9)
                .muscleWeight(99.99)
                .gender(Gender.MALE)
                .build();

        // when
        UserResponse response = userService.edit(default1.getId(), request);

        // then
        assertThat(response.getEmail()).isEqualTo(default1.getEmail());
        assertThat(response.getUniName()).isEqualTo(default1.getUniName());
        assertThat(response.getName()).isEqualTo(default1.getName());
        assertThat(response.getNickname()).isEqualTo(default1.getNickname());
        assertThat(response.getAuthProvider()).isEqualTo(default1.getAuthProvider());
        assertThat(response.getRole()).isEqualTo(default1.getRole());
        assertThat(response.getProfilePhoto()).isEqualTo(default1.getProfilePhoto());
        assertThat(response.getEmailVerify()).isEqualTo(default1.getEmailVerify());
        assertThat(response.getLevel()).isEqualTo(default1.getLevel());
        assertThat(response.getHeight()).isEqualTo(default1.getHeight());
        assertThat(response.getBirth()).isEqualTo(DateUtils.toDate(default1.getBirth()));
        assertThat(response.getWeight()).isEqualTo(default1.getWeight());
        assertThat(response.getMuscleWeight()).isEqualTo(default1.getMuscleWeight());
        assertThat(response.getGender()).isEqualTo(default1.getGender());

    }
}
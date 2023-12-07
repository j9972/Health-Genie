package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.TrainerProfileException;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestSyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TrainerProfileServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TrainerProfileService trainerProfileService;

    User user;
    User user2;
    TrainerInfo profile;

    @BeforeEach
    void before() {
        user = testSyUtils.createUser("test", Role.USER,"jh485200@gmail.com");
        user2 = testSyUtils.createUser("test2", Role.TRAINER,"test@gmail.com");
        profile = testSyUtils.createProfile("introduction", "career", 10000,20,user);
    }

    @Test
    @DisplayName("정상적으로 profile 작성")
    void writeProfile() {
        // given
        testSyUtils.login(user);

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", 12000, 25, "test");

        // when
        ProfileResponseDto response = trainerProfileService.writeProfile(dto);

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getCost()).isEqualTo(12000);
        assertThat(response.getMonth()).isEqualTo(25);
        assertThat(response.getNickname()).isEqualTo("test");

    }

    @Test
    @DisplayName("로그인 하지 않은 유저 profile 작성")
    void notLoginWriteProfile() {
        // given
        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", 12000, 25, "test");

        // when

        // then
        assertThatThrownBy(() -> trainerProfileService.writeProfile(dto))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("정상적으로 profile 수정")
    void updateProfile() {
        // given
        testSyUtils.login(user);

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", 12000, 25, "test");

        // when
        ProfileResponseDto response = trainerProfileService.updateProfile(dto, profile.getId());

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getCost()).isEqualTo(12000);
        assertThat(response.getMonth()).isEqualTo(25);
        assertThat(response.getNickname()).isEqualTo("test");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 profile 수정")
    void notLoginUpdateProfile() {
        // given
        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", 12000, 25, "test");

        // when

        // then
        assertThatThrownBy(() -> trainerProfileService.updateProfile(dto,profile.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("다른 유저가 profile 수정")
    void notOwnUpdateProfile() {
        // given
        testSyUtils.login(user);

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", 12000, 25, "test");

        // when
        ProfileResponseDto saved = trainerProfileService.writeProfile(dto);
        ProfileRequestDto updatedDto = testSyUtils.createProfileDto("update intro", "update", 12000, 25, "test");

        testSyUtils.login(user2);

        // then
        assertThatThrownBy(() -> trainerProfileService.updateProfile(updatedDto, saved.getId()))
                .isInstanceOf(TrainerProfileException.class);
    }

    @Test
    @DisplayName("정상적으로 profile 조회")
    void getProfile() {
        // given
        testSyUtils.login(user);

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", 12000, 25, "test");

        // when
        ProfileResponseDto response = trainerProfileService.writeProfile(dto);
        trainerProfileService.getProfile(response.getId());

        // then
        assertThat(response.getNickname()).isEqualTo(dto.getNickname());
    }
}
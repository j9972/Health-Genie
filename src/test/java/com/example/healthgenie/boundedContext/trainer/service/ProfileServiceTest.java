package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.profile.service.ProfileService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.service.UserService;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProfileServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    User user;
    User user2;
    TrainerInfo profile;

    @BeforeEach
    void before() {
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        user = testKrUtils.createUser("jh485200@gmail.com", "test", AuthProvider.EMPTY, Role.USER);
        user2 = testKrUtils.createUser("test@gmail.com", "test2", AuthProvider.EMPTY, Role.TRAINER);

        userService.update(user, null, "test", null, null, null, null, null);
        userService.update(user2, null, "test2", null, null, null, null, null);

        profile = testSyUtils.createProfile("introduction", "career", "경북대", startTime, endTime, 4.3, 20000, 20, user2);
    }


    @Test
    @DisplayName("profile 작성")
    void save() {
        // given
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대", startTime, endTime, 4.5,
                12000, 25, "test2");

        // when
        ProfileResponseDto response = profileService.save(user2, dto);

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getUniversity()).isEqualTo("경북대");
        assertThat(response.getStartTime()).isEqualTo(startTime);
        assertThat(response.getEndTime()).isEqualTo(endTime);
        assertThat(response.getReviewAvg()).isEqualTo(4.5);
        assertThat(response.getCost()).isEqualTo(12000);
        assertThat(response.getMonth()).isEqualTo(25);
        assertThat(response.getNickname()).isEqualTo("test2");

    }

    @Test
    @DisplayName("로그인 하지 않은 유저 profile 작성")
    void fail_write_profile_cuz_of_login() {
        // given
        testSyUtils.logout();

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대",
                startTime, endTime, 4.5, 12000, 25, "test2");

        // when

        // then
        assertThatThrownBy(() -> {
            // 해당 메소드 호출
            profileService.save(user, dto);
        }).isInstanceOf(CustomException.TRAINER_INFO_EMPTY.getClass());
    }

//    @Test
//    @DisplayName("정상적으로 profile 수정")
//    void update_profile() {
//        // given
//        testKrUtils.login(user2);
//
//        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
//        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초
//
//        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대",
//                startTime, endTime, 4.5, 2000, 25, "test2");
//
//        // when
//        ProfileResponseDto response = profileService.updateProfile(dto, profile.getId(), user2);
//
//        // then
//        assertThat(response.getIntroduction()).isEqualTo("test intro");
//        assertThat(response.getCareer()).isEqualTo("none");
//        assertThat(response.getUniversity()).isEqualTo("경북대");
//        assertThat(response.getStartTime()).isEqualTo(startTime);
//        assertThat(response.getEndTime()).isEqualTo(endTime);
//        assertThat(response.getReviewAvg()).isEqualTo(4.5);
//        assertThat(response.getPhotoPaths()).isEmpty();
//        assertThat(response.getCost()).isEqualTo(12000);
//        assertThat(response.getMonth()).isEqualTo(25);
//        assertThat(response.getNickname()).isEqualTo("test2");
//    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 profile 수정")
    void fail_update_profile_cuz_of_login() {
        // given
        testSyUtils.logout();

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대",
                startTime, endTime, 4.5, 12000, 25, "test2");

        // when

        // then
        assertThatThrownBy(() -> {
            // 해당 메소드 호출
            profileService.updateProfile(dto, profile.getId(), user);
        }).isInstanceOf(CustomException.TRAINER_INFO_EMPTY.getClass());
    }

//    @Test
//    @DisplayName("해당 트레이너가 아닌 다른 트레이너 혹은 다른 회원이 profile 수정")
//    void fail_update_profile_cuz_of_role() {
//        // given
//        testKrUtils.login(user);
//
//        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
//        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초
//
//        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대",
//                startTime, endTime, 4.5, null, 12000, 25, "test");
//
//        // when
//        ProfileResponseDto saved = trainerProfileService.save(user, dto, null);
//        ProfileRequestDto updatedDto = testSyUtils.createProfileDto("updated intro", "Olympia", "경북대",
//                startTime, endTime, 4.5, null, 22000, 25, "test");
//
//        testKrUtils.login(user2);
//
//        // then
//        assertThatThrownBy(() -> trainerProfileService.updateProfile(updatedDto, saved.getId(), user2, null))
//                .isInstanceOf(CustomException.class);
//    }

//    @Test
//    @DisplayName("정상적으로 profile 조회")
//    void get_profile() {
//        // given
//        testKrUtils.login(user);
//
//        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
//        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초
//
//        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대",
//                startTime, endTime, 4.5, null, 12000, 25, "test");
//
//        // when
//        ProfileResponseDto response = trainerProfileService.save(user, dto, null);
//        trainerProfileService.getProfile(response.getId());
//
//        // then
//        assertThat(response.getNickname()).isEqualTo(dto.getNickname());
//    }
}
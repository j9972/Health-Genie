package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.TrainerProfileErrorResult;
import com.example.healthgenie.base.exception.TrainerProfileException;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
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
class TrainerProfileServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    TrainerProfileService trainerProfileService;

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

        userService.update(user, UserRequest.builder().nickname("test").build());
        userService.update(user2, UserRequest.builder().nickname("test2").build());

        profile = testSyUtils.createProfile("introduction", "career", "경북대",startTime, endTime, 4.3, null,20000,20, user2);
    }


    @Test
    @DisplayName("profile 작성")
    void save() {
        // given
        testKrUtils.login(user2);

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none","경북대",
                startTime,endTime,4.5,null, 12000, 25, "test2");

        // when
        ProfileResponseDto response = trainerProfileService.save(dto,user2);

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getUniversity()).isEqualTo("경북대");
        assertThat(response.getStartTime()).isEqualTo(startTime);
        assertThat(response.getEndTime()).isEqualTo(endTime);
        assertThat(response.getReviewAvg()).isEqualTo(4.5);
        assertThat(response.getPhotoPaths()).isEmpty();
        assertThat(response.getCost()).isEqualTo(12000);
        assertThat(response.getMonth()).isEqualTo(25);
        assertThat(response.getNickname()).isEqualTo("test2");

    }

    @Test
    @DisplayName("로그인 하지 않은 유저 profile 작성")
    void notLoginWriteProfile() {
        // given
        boolean login = testSyUtils.notLogin(user);

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none","경북대",
                startTime,endTime,4.5,null, 12000, 25, "test2");

        // when

        // then
        assertThatThrownBy(() -> {
            if(!login) {
                throw new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY);
            } else {
                trainerProfileService.save(dto, user);
            }
        });
    }

    @Test
    @DisplayName("정상적으로 profile 수정")
    void updateProfile() {
        // given
        testKrUtils.login(user2);

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none","경북대",
                startTime,endTime,4.5,null, 12000, 25, "test2");

        // when
        ProfileResponseDto response = trainerProfileService.updateProfile(dto, profile.getId(),user2);

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getUniversity()).isEqualTo("경북대");
        assertThat(response.getStartTime()).isEqualTo(startTime);
        assertThat(response.getEndTime()).isEqualTo(endTime);
        assertThat(response.getReviewAvg()).isEqualTo(4.5);
        assertThat(response.getPhotoPaths()).isEmpty();
        assertThat(response.getCost()).isEqualTo(12000);
        assertThat(response.getMonth()).isEqualTo(25);
        assertThat(response.getNickname()).isEqualTo("test2");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 profile 수정")
    void notLoginUpdateProfile() {
        // given
        boolean login = testSyUtils.notLogin(user);

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none","경북대",
                startTime,endTime,4.5,null, 12000, 25, "test2");

        // when

        // then
        assertThatThrownBy(() -> {
            if(!login) {
                throw new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY);
            } else {
                trainerProfileService.updateProfile(dto,profile.getId(),user);
            }
        });
    }

    @Test
    @DisplayName("해당 트레이너가 아닌 다른 트레이너 혹은 다른 회원이 profile 수정")
    void notOwnUpdateProfile() {
        // given
        testKrUtils.login(user);

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none","경북대",
                startTime,endTime,4.5,null, 12000, 25, "test");

        // when
        ProfileResponseDto saved = trainerProfileService.save(dto, user);
        ProfileRequestDto updatedDto = testSyUtils.createProfileDto("updated intro", "Olympia","경북대",
                startTime,endTime,4.5,null, 22000, 25, "test");


        testKrUtils.login(user2);

        // then
        assertThatThrownBy(() -> trainerProfileService.updateProfile(updatedDto, saved.getId(), user2))
                .isInstanceOf(TrainerProfileException.class);
    }

    @Test
    @DisplayName("정상적으로 profile 조회")
    void getProfile() {
        // given
        testKrUtils.login(user);

        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none","경북대",
                startTime,endTime,4.5,null, 12000, 25, "test");

        // when
        ProfileResponseDto response = trainerProfileService.save(dto, user);
        trainerProfileService.getProfile(response.getId());

        // then
        assertThat(response.getNickname()).isEqualTo(dto.getNickname());
    }
}
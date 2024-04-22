package com.example.healthgenie.boundedContext.trainer.service;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.entity.enums.PurposeOfUsing;
import com.example.healthgenie.boundedContext.trainer.photo.service.ProfilePhotoService;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileDeleteResponseDto;
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
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

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
    ProfilePhotoService photoService;

    @Autowired
    UserService userService;

    User user;
    User user2;
    User user3;
    TrainerInfo profile;
    TrainerPhoto photo;

    @BeforeEach
    void before() {
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        user = testKrUtils.createUser("jh485200@gmail.com", "test", AuthProvider.EMPTY, Role.USER);
        user2 = testKrUtils.createUser("test@gmail.com", "test2", AuthProvider.EMPTY, Role.TRAINER);
        user3 = testKrUtils.createUser("test3@gmail.com", "test3", AuthProvider.EMPTY, Role.TRAINER);

        userService.update(user, null, "test", null, null, null, null, null);
        userService.update(user2, null, "test2", null, null, null, null, null);

        profile = testSyUtils.createProfile("introduction", "career", "경북대", startTime, endTime, 4.3, 20000, 20, user2);
        photo = testSyUtils.createProfilePhoto(profile, "test", "test", PurposeOfUsing.PROFILE);
    }

    @Test
    @DisplayName("profile 작성")
    void save() {
        // given
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대", startTime, endTime, 4.5,
                12000, 25, "test3");

        // when
        ProfileResponseDto response = profileService.save(user3, dto);

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getUniversity()).isEqualTo("경북대");
        assertThat(response.getStartTime()).isEqualTo(startTime);
        assertThat(response.getEndTime()).isEqualTo(endTime);
        assertThat(response.getReviewAvg()).isEqualTo(4.5);
        assertThat(response.getCost()).isEqualTo(12000);
        assertThat(response.getMonth()).isEqualTo(25);
    }

    @Test
    @DisplayName("profile 작성 - 권한 실패")
    void fail_no_permission_for_saving() {
        // given
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대", startTime, endTime, 4.5,
                12000, 25, "test2");

        // when
        ProfileResponseDto response = profileService.save(user3, dto);

        // then
        assertThatThrownBy(() -> {
            if (!user.getRole().equals(Role.TRAINER)) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("정상적으로 profile 수정")
    void update_profile() {
        // given
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        ProfileRequestDto dto = testSyUtils.createProfileDto("test intro", "none", "경북대",
                startTime, endTime, 4.5, 2000, 25, "test2");

        // when
        ProfileResponseDto response = profileService.updateProfile(dto, profile.getId(), user2);

        // then
        assertThat(response.getIntroduction()).isEqualTo("test intro");
        assertThat(response.getCareer()).isEqualTo("none");
        assertThat(response.getUniversity()).isEqualTo("경북대");
        assertThat(response.getStartTime()).isEqualTo(startTime);
        assertThat(response.getEndTime()).isEqualTo(endTime);
        assertThat(response.getReviewAvg()).isEqualTo(4.5);
        assertThat(response.getPhotoPaths()).isEmpty();
        assertThat(response.getCost()).isEqualTo(2000);
        assertThat(response.getMonth()).isEqualTo(25);
        assertThat(response.getNickname()).isEqualTo("test2");
    }

    @Test
    @DisplayName("해당 트레이너가 아닌 다른 트레이너 혹은 다른 회원이 profile 수정")
    void fail_update_profile_cuz_of_role() {
        // given
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        // when
        ProfileRequestDto updatedDto = testSyUtils.createProfileDto("updated intro", "Olympia", "경북대",
                startTime, endTime, 4.5, 22000, 25, "test");

        // then
        assertThatThrownBy(() -> profileService.updateProfile(updatedDto, profile.getId(), user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("정상적으로 profile 조회")
    void get_profile() {
        // given

        // when
        ProfileResponseDto response = profileService.getProfile(profile.getId());

        // then
        assertThat(response.getId()).isEqualTo(profile.getId());
        assertThat(response.getIntroduction()).isEqualTo("introduction");
        assertThat(response.getCareer()).isEqualTo("career");
        assertThat(response.getUniversity()).isEqualTo("경북대");
        assertThat(response.getCost()).isEqualTo(20000);
        assertThat(response.getMonth()).isEqualTo(20);
        assertThat(response.getReviewAvg()).isEqualTo(4.3);
    }

    @Test
    @DisplayName("profile 조회 실패 - 권한 실패")
    void fail_no_permission_for_get_profile() {
        // given

        // when
        ProfileResponseDto response = profileService.getProfile(profile.getId());

        // then
        assertThatThrownBy(() -> {
            if (response.getUserId() != user.getId()) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("profile 검색 성공")
    void find_all() {
        // given
        String keyword = "profile";

        // when
        Slice<TrainerInfo> all = profileService.findAll(keyword, 1L, Pageable.ofSize(10));

        // then
        assertThat(all.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("profile 조회 실패하기")
    void fail_find_all() {
        // given
        String keyword = "test";

        // when
        Slice<TrainerInfo> response = profileService.findAll(keyword, 0L, Pageable.ofSize(10));

        // then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("profile 삭제 성공")
    void delete_profile() {
        // given

        // when
        ProfileDeleteResponseDto response = profileService.deleteProfile(profile.getId(), user2);

        // then
        assertThat(response.getId()).isEqualTo(profile.getId());
    }

    @Test
    @DisplayName("profile 삭제 실패")
    void fail_trainer_delete_pt_profile_cuz_of_role() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!user2.getRole().equals(Role.USER)) {
                throw new CustomException(DATA_NOT_FOUND);
            }
        }).isInstanceOf(CustomException.class);
    }
}
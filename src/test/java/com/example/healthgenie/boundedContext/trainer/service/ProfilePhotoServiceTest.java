package com.example.healthgenie.boundedContext.trainer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.service.ProfilePhotoService;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProfilePhotoServiceTest {

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    ProfilePhotoService profilePhotoService;

    TrainerInfo profile;
    User user;
    TrainerPhoto photo;

    @BeforeEach
    void before() {
        LocalTime startTime = LocalTime.of(14, 0, 0); // 시, 분, 초
        LocalTime endTime = LocalTime.of(15, 0, 0); // 시, 분, 초

        user = testKrUtils.createUser("test1@test.com", "test1", AuthProvider.EMPTY, Role.TRAINER);
        profile = testSyUtils.createProfile("test intro", "test career", "경북대", startTime, endTime, 4.5, 12000, 23,
                user);
        photo = testSyUtils.createProfilePhoto(profile, "default-path.png");
    }

    @Test
    @DisplayName("단일 사진 저장하기")
    void save() {
        // given

        // when
        TrainerPhoto saved = profilePhotoService.save(profile.getId(), "test-photo-path.png");

        // then
        assertThat(saved.getInfo().getId()).isEqualTo(profile.getId());
        assertThat(saved.getInfoPhotoPath()).isEqualTo("test-photo-path.png");
    }

    @Test
    @DisplayName("존재하지 않은 사진 저장하기")
    void notExistSave() {
        // given

        // when

        // then
        assertThatThrownBy(() -> profilePhotoService.save(999L, "test-photo-path.png"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("다중 사진 저장하기")
    void saveAll() {
        // given

        // when
        List<TrainerPhoto> saved = profilePhotoService.saveAll(profile.getId(),
                List.of("test-photo-path.png", "test1-photo-path.png", "test2-photo-path.png"));

        // then
        assertThat(saved.size()).isEqualTo(3);
        assertThat(saved).extracting(TrainerPhoto::getInfoPhotoPath)
                .containsExactly("test-photo-path.png", "test1-photo-path.png", "test2-photo-path.png");

    }

    @Test
    @DisplayName("다중 사진 수정하기")
    void updateAll() {
        // given
        profilePhotoService.updateAll(profile.getId(),
                List.of("updated-photo-path.png", "updated1-photo-path.png", "updated2-photo-path.png"));

        // when
        List<TrainerPhoto> all = profilePhotoService.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
        assertThat(all).extracting(TrainerPhoto::getInfoPhotoPath)
                .containsExactly("updated-photo-path.png", "updated1-photo-path.png", "updated2-photo-path.png");
    }

    @Test
    @DisplayName("다중 사진 조회하기")
    void findAll() {
        // given
        profilePhotoService.saveAll(profile.getId(),
                List.of("test-photo-path.png", "test1-photo-path.png", "test2-photo-path.png"));

        // when
        List<TrainerPhoto> all = profilePhotoService.findAll();

        // then
        assertThat(all.size()).isEqualTo(4);
        assertThat(all).extracting(TrainerPhoto::getInfoPhotoPath)
                .containsExactly("default-path.png", "test-photo-path.png", "test1-photo-path.png",
                        "test2-photo-path.png");
    }
}
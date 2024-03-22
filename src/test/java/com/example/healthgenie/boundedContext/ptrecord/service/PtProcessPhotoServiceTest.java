package com.example.healthgenie.boundedContext.ptrecord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.PtProcess.PtProcessException;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PtProcessPhotoServiceTest {
    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    PtProcessPhotoService ptProcessPhotoService;

    PtProcess process;
    User user;
    User user2;
    PtProcessPhoto photo;

    @BeforeEach
    void before() {
        user = testKrUtils.createUser("test1@test.com", "test1", AuthProvider.EMPTY, Role.USER);
        user2 = testKrUtils.createUser("test2@test.com", "test2", AuthProvider.EMPTY, Role.TRAINER);
        process = testSyUtils.createProcess("title", "content", user, user2);
        photo = testSyUtils.createProcessPhoto(process, "default-path.png");
    }

    @Test
    @DisplayName("정상적으로 단일 사진(경로) 저장하기")
    void save() {
        // given

        // when
        PtProcessPhoto savedPhoto = ptProcessPhotoService.save(process.getId(), "test-path1.png");

        // then
        assertThat(savedPhoto.getProcess().getId()).isEqualTo(process.getId());
        assertThat(savedPhoto.getProcessPhotoPath()).isEqualTo("test-path1.png");
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 사진(경로) 저장하기")
    void notExistPostSavePhotoPath() {
        // given

        // when

        // then
        assertThatThrownBy(() -> ptProcessPhotoService.save(999L, "test-path1.png"))
                .isInstanceOf(PtProcessException.class);
    }

    @Test
    @DisplayName("정상적으로 다중 사진(경로) 저장하기")
    void saveAll() {
        // given

        // when
        List<PtProcessPhoto> savedPhotos =
                ptProcessPhotoService.saveAll(process.getId(),
                        List.of("test-path1.png", "test-path2.png", "test-path3.png"));

        // then
        assertThat(savedPhotos.size()).isEqualTo(3);
        assertThat(savedPhotos).extracting(PtProcessPhoto::getProcessPhotoPath)
                .containsExactly("test-path1.png", "test-path2.png", "test-path3.png");
    }
}
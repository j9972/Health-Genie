package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CommunityPostPhotoServiceTest {

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    CommunityPostPhotoService communityPostPhotoService;

    CommunityPost post1;
    User user1;
    CommunityPostPhoto photo1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("test1", Role.EMPTY, "test1@test.com");
        post1 = testKrUtils.createPost(user1, "테스트 게시글 제목1", "테스트 게시글 내용1");
        photo1 = testKrUtils.createPostPhoto(post1, "default-path.png");
    }

    @Test
    @DisplayName("정상적으로 단일 사진(경로) 저장하기")
    void save() {
        // given

        // when
        CommunityPostPhoto savedPhoto = communityPostPhotoService.save(post1.getId(), "test-path1.png");

        // then
        assertThat(savedPhoto.getPost().getId()).isEqualTo(post1.getId());
        assertThat(savedPhoto.getPostPhotoPath()).isEqualTo("test-path1.png");
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 사진(경로) 저장하기")
    void notExistPostSavePhotoPath() {
        // given

        // when

        // then
        assertThatThrownBy(() -> communityPostPhotoService.save(999L, "test-path1.png"))
                .isInstanceOf(CommunityPostException.class);
    }

    @Test
    @DisplayName("정상적으로 다중 사진(경로) 저장하기")
    void saveAll() {
        // given

        // when
        List<CommunityPostPhoto> savedPhotos =
                communityPostPhotoService.saveAll(post1.getId(), List.of("test-path1.png", "test-path2.png", "test-path3.png"));

        // then
        assertThat(savedPhotos.size()).isEqualTo(3);
        assertThat(savedPhotos).extracting(CommunityPostPhoto::getPostPhotoPath)
                .containsExactly("test-path1.png", "test-path2.png", "test-path3.png");
    }

    @Test
    @DisplayName("게시글의 모든 사진(경로) 가져오기")
    void findAll() {
        // given
        communityPostPhotoService.saveAll(post1.getId(), List.of("test-path1.png", "test-path2.png", "test-path3.png"));

        // when
        List<CommunityPostPhoto> findPhotos = communityPostPhotoService.findAll();

        // then
        assertThat(findPhotos.size()).isEqualTo(4);
        assertThat(findPhotos).extracting(CommunityPostPhoto::getPostPhotoPath)
                .containsExactly("default-path.png", "test-path1.png", "test-path2.png", "test-path3.png");
    }

    @Test
    @DisplayName("정상적으로 사진(경로) 수정하기")
    void updateAll() {
        // given
        communityPostPhotoService.updateAll(post1.getId(), List.of("update-path1.png", "update-path2.png"));

        // when
        List<CommunityPostPhoto> findPhotos = communityPostPhotoService.findAll();

        // then
        assertThat(findPhotos.size()).isEqualTo(2);
        assertThat(findPhotos).extracting(CommunityPostPhoto::getPostPhotoPath)
                .containsExactly("update-path1.png", "update-path2.png");
    }

    @Test
    @DisplayName("존재하지 않는 게시글의 사진(경로) 수정하기")
    void notExistPostEditPhoto() {
        // given

        // when

        // then
        assertThatThrownBy(() -> communityPostPhotoService.updateAll(999L, List.of("update-path1.png", "update-path2.png")))
                .isInstanceOf(CommunityPostException.class);
    }
}
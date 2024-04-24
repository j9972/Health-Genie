package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.community.like.entity.Like;
import com.example.healthgenie.boundedContext.community.like.service.LikeService;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
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
class LikeServiceTest {

    @Autowired
    LikeService likeService;
    @Autowired
    TestKrUtils testKrUtils;

    Post post1;
    User user1;
    User user2;
    Like user2ToPost1Like;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1@test.com", "user1", AuthProvider.KAKAO, Role.USER);
        user2 = testKrUtils.createUser("user2@test.com", "user2", AuthProvider.GOOGLE, Role.TRAINER);
        post1 = testKrUtils.createPost(user1.getId(), "기본 제목", "기본 내용");
        user2ToPost1Like = testKrUtils.createLike(post1.getId(), user2);
    }

    @Test
    @DisplayName("좋아요 표시")
    void save() {
        // given

        // when
        Like like = likeService.save(post1.getId(), user1);

        // then
        assertThat(like.getPost()).isEqualTo(post1);
        assertThat(like.getUser()).isEqualTo(user1);
    }

    @Test
    @DisplayName("같은 게시글에 중복된 좋아요 표시 불가능")
    void save_duplicate_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> likeService.save(post1.getId(), user2))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 좋아요 표시 불가능")
    void save_notExistsPost_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> likeService.save(999L, user1))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("해당 게시글의 모든 좋아요 조회")
    void findAllByPostId() {
        // given

        // when
        List<Like> likes = likeService.findAllByPostId(post1.getId());

        // then
        assertThat(likes).extracting(Like::getUser).contains(user2);
    }

    @Test
    @DisplayName("해당 게시글의 좋아요 개수 조회")
    void countByPostId() {
        // given

        // when
        Long count = likeService.countByPostId(post1.getId());

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 표시 삭제")
    void delete() {
        // given

        // when
        likeService.delete(post1.getId(), user2.getId());

        // then
        assertThat(likeService.countByPostId(post1.getId())).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 좋아요 표시 삭제")
    void delete_notExistsLike_exception() {
        // given

        // when

        // then
        assertThatThrownBy(() -> likeService.delete(post1.getId(), user1.getId()))
                .isInstanceOf(CustomException.class);
    }
}
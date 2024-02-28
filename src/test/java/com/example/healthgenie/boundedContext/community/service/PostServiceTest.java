package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    PostService postService;

    User user1;
    User user2;
    Post post1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("test1", Role.EMPTY, "test1@test.com");
        user2 = testKrUtils.createUser("test2", Role.EMPTY, "test2@test.com");
        post1 = testKrUtils.createPost(user1, "초기 게시글 제목", "초기 게시글 내용");
    }

    @Test
    @DisplayName("정상적인 게시글 생성하기")
    void createPost() {
        // given
        testKrUtils.login(user1);

        PostRequest request = testKrUtils.createPostRequest("테스트 게시글 제목1", "테스트 게시글 내용1");

        // when
        Post response = postService.save(user1.getId(), request);

        // then
        assertThat(response.getTitle()).isEqualTo("테스트 게시글 제목1");
        assertThat(response.getContent()).isEqualTo("테스트 게시글 내용1");
        assertThat(response.getWriter().getNickname()).isEqualTo(user1.getNickname());
    }

    @Test
    @DisplayName("정상적인 게시글 조회하기")
    void findById() {
        // given

        PostRequest request = testKrUtils.createPostRequest("테스트 게시글 제목1", "테스트 게시글 내용1");
        Post savedPost = postService.save(user1.getId(), request);

        // when
        Post response = postService.findById(savedPost.getId());

        // then
        assertThat(response.getTitle()).isEqualTo("테스트 게시글 제목1");
        assertThat(response.getContent()).isEqualTo("테스트 게시글 내용1");
        assertThat(response.getWriter().getNickname()).isEqualTo(user1.getNickname());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회하기")
    void notExistPost() {
        // given

        // when

        // then
        assertThatThrownBy(() -> postService.findById(999L))
                .isInstanceOf(CommunityPostException.class);
    }

    @Test
    @DisplayName("정상적인 게시글 전체 조회하기")
    void findAll() {
        // given
        for(int i=1; i<=10; i++) {
            PostRequest request = testKrUtils.createPostRequest("테스트 게시글 제목" + i, "테스트 게시글 내용" + i);
            postService.save(user1.getId(), request);
        }

        // when
        List<Post> response = postService.findAll("");

        // then
        assertThat(response.size()).isEqualTo(11);
        assertThat(response).isSortedAccordingTo(Comparator.comparingLong(Post::getId).reversed());
    }

    @Test
    @DisplayName("정상적인 게시글 제목 키워드 조회하기")
    void findAllByKeyword() {
        // given
        for(int i=1; i<=10; i++) {
            PostRequest request = testKrUtils.createPostRequest("테스트 게시글 제목" + i, "테스트 게시글 내용" + i);
            postService.save(user1.getId(), request);
        }

        for(int i=1; i<=3; i++) {
            PostRequest request = testKrUtils.createPostRequest("키워드입니다." + i, "테스트 게시글 내용" + i);
            postService.save(user1.getId(), request);
        }

        // when
        String keyword = "키워드";
        List<Post> response = postService.findAll(keyword);

        // then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response).isSortedAccordingTo(Comparator.comparingLong(Post::getId).reversed());
        assertThat(response).extracting(Post::getTitle).doesNotContain("테스트 게시글 제목");
    }

    @Test
    @DisplayName("정상적인 게시글 수정하기")
    void update() {
        // given
        testKrUtils.login(user1);

        // when
        PostRequest updateRequest = testKrUtils.createPostRequest("수정한 제목", "수정한 내용");

        // then
        Post response = postService.update(post1.getId(), user1.getId(), updateRequest);

        assertThat(response.getTitle()).isEqualTo("수정한 제목");
        assertThat(response.getContent()).isEqualTo("수정한 내용");
    }

    @Test
    @DisplayName("자신의 게시글이 아닌 게시글을 수정하기")
    void notOwnUpdate() {
        // given

        // when
        PostRequest updateRequest = testKrUtils.createPostRequest("수정한 제목", "수정한 내용");

        // then
        assertThatThrownBy(() -> postService.update(post1.getId(), user2.getId(), updateRequest))
                .isInstanceOf(CommunityPostException.class);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 수정하기")
    void notExistUpdate() {
        // given

        // when
        PostRequest updateRequest = testKrUtils.createPostRequest("수정한 제목", "수정한 내용");

        // then
        assertThatThrownBy(() -> postService.update(999L, user1.getId(), updateRequest))
                .isInstanceOf(CommunityPostException.class);
    }

    @Test
    @DisplayName("정상적인 게시글 삭제하기")
    void delete() {
        // given

        // when

        // then
        String response = postService.delete(post1.getId(), user1.getId());

        assertThat(response).isEqualTo("게시글이 삭제 되었습니다.");
    }

    @Test
    @DisplayName("자신의 게시글이 아닌 게시글을 삭제하기")
    void notOwnDelete() {
        // given

        // when

        // then
        assertThatThrownBy(() -> postService.delete(post1.getId(), user2.getId()))
                .isInstanceOf(CommunityPostException.class);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 삭제하기")
    void notExistDelete() {
        // given

        // when

        // then
        assertThatThrownBy(() -> postService.delete(999L, user1.getId()))
                .isInstanceOf(CommunityPostException.class);
    }
}
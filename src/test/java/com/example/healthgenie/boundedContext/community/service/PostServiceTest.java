package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestKrUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

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
        user1 = testKrUtils.createUser("user1@test.com", "user1", AuthProvider.KAKAO, Role.USER);
        user2 = testKrUtils.createUser("user2@test.com", "user2", AuthProvider.KAKAO, Role.USER);
        post1 = testKrUtils.createPost(user1.getId(), "기본 제목", "기본 내용");
    }

    @Test
    @DisplayName("게시글 저장")
    void save() {
        // given
        PostRequest request = PostRequest.builder().title("제목").content("내용").build();

        // when
        Post savePost = postService.save(user1.getId(), request);

        // then
        assertThat(savePost.getWriter()).isEqualTo(user1);
        assertThat(savePost.getTitle()).isEqualTo("제목");
        assertThat(savePost.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("게시글 단건(Entity) 조회 - id")
    void findById() {
        // given

        // when
        Post findPost = postService.findById(post1.getId());

        // then
        assertThat(findPost.getId()).isEqualTo(post1.getId());
        assertThat(findPost.getWriter()).isEqualTo(post1.getWriter());
        assertThat(findPost.getTitle()).isEqualTo(post1.getTitle());
        assertThat(findPost.getContent()).isEqualTo(post1.getContent());
    }

    @Test
    @DisplayName("게시글 단건(DTO) 조회 - id")
    void findDtoById() {
        // given

        // when
        PostResponse findPost = postService.findDtoById(post1.getId());

        // then
        assertThat(findPost.getId()).isEqualTo(post1.getId());
        assertThat(findPost.getWriter()).isEqualTo(post1.getWriter().getNickname());
        assertThat(findPost.getTitle()).isEqualTo(post1.getTitle());
        assertThat(findPost.getContent()).isEqualTo(post1.getContent());
    }

    @Test
    @DisplayName("게시글 전체 조회 - 무한 스크롤")
    void findAll() {
        // given
        Long start = 0L;
        Long end = 0L;
        for(int i=1; i<=20; i++) {
            Post post = testKrUtils.createPost(user1.getId(), "검색용" + i);
            if(i == 1) {
                start = post.getId();
            }
            if(i == 20) {
                end = post.getId();
            }
        }

        // when
        Slice<Post> findPosts1 = postService.findAll("검색용", null, null, PageRequest.of(0, 10));
        Slice<Post> findPosts2 = postService.findAll("검색용", null, (start+end)/2+1, PageRequest.of(0, 10));

        // then
        assertThat(findPosts1.isLast()).isFalse();
        assertThat(findPosts1.getContent().size()).isEqualTo(10);
        assertThat(findPosts2.isLast()).isTrue();
        assertThat(findPosts2.getContent().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시글 수정")
    void update() {
        // given
        PostRequest request = PostRequest.builder().title("수정된 제목").content("수정된 내용").build();

        // when
        Post updatePost = postService.update(post1.getId(), user1.getId(), request);

        // then
        assertThat(updatePost.getId()).isEqualTo(post1.getId());
        assertThat(updatePost.getWriter()).isEqualTo(post1.getWriter());
        assertThat(updatePost.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatePost.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() {
        // given

        // when
        postService.delete(post1.getId(), user1.getId());

        // then
        assertThatThrownBy(() -> postService.findById(post1.getId()))
                .isInstanceOf(CommunityPostException.class);
    }
}
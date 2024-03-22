package com.example.healthgenie.boundedContext.community.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.healthgenie.base.exception.Comment.CommunityCommentException;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.comment.service.CommentService;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    TestKrUtils testKrUtils;

    Post post1;
    User user1;
    Comment comment1;

    @BeforeEach
    void before() {
        user1 = testKrUtils.createUser("user1@test.com", "user1", AuthProvider.KAKAO, Role.USER);
        post1 = testKrUtils.createPost(user1.getId(), "기본 제목", "기본 내용");
        comment1 = testKrUtils.createComment(post1.getId(), user1, "기본 댓글");
    }

    @Test
    @DisplayName("댓글 저장")
    void save() {
        // given
        CommentRequest request = CommentRequest.builder().content("새 댓글").build();

        // when
        Comment saveComment = commentService.save(post1.getId(), user1, request);

        // then
        assertThat(saveComment.getPost()).isEqualTo(post1);
        assertThat(saveComment.getWriter()).isEqualTo(user1);
        assertThat(saveComment.getContent()).isEqualTo("새 댓글");
    }

    @Test
    @DisplayName("댓글 단건 조회 - id")
    void findById() {
        // given

        // when
        Comment findComment = commentService.findById(comment1.getId());

        // then
        assertThat(findComment.getId()).isEqualTo(comment1.getId());
        assertThat(findComment.getPost()).isEqualTo(comment1.getPost());
        assertThat(findComment.getWriter()).isEqualTo(comment1.getWriter());
        assertThat(findComment.getContent()).isEqualTo(comment1.getContent());
    }

    @Test
    @DisplayName("전체 댓글 조회 - 전체 게시글")
    void findAll() {
        // given
        for (int i = 0; i < 10; i++) {
            Post post = testKrUtils.createPost(user1.getId(), "제목" + i, "내용" + i);
            testKrUtils.createComment(post.getId(), user1, "댓글" + i);
        }

        // when
        List<Comment> findComments = commentService.findAll();

        // then
        assertThat(findComments.size()).isEqualTo(11);
    }

    @Test
    @DisplayName("전체 댓글(Entity) 조회 - 해당 게시글")
    void findAllByPostId() {
        // given
        for (int i = 0; i < 10; i++) {
            testKrUtils.createComment(post1.getId(), user1, "댓글" + i);
        }

        // when
        List<Comment> findComments = commentService.findAllByPostId(post1.getId());

        // then
        assertThat(findComments.size()).isEqualTo(11);
    }

    @Test
    @DisplayName("전체 댓글(DTO) 조회 - 해당 게시글")
    void findDtosAllByPostId() {
        // given
        for (int i = 0; i < 10; i++) {
            testKrUtils.createComment(post1.getId(), user1, "댓글" + i);
        }

        // when
        List<CommentResponse> findComments = commentService.findDtosAllByPostId(post1.getId());

        // then
        assertThat(findComments.size()).isEqualTo(11);
    }

    @Test
    @DisplayName("댓글 수정")
    void update() {
        // given
        CommentRequest request = CommentRequest.builder().content("수정된 댓글").build();

        // when
        Comment updateComment = commentService.update(post1.getId(), comment1.getId(), user1.getId(), request);

        // then
        assertThat(updateComment.getId()).isEqualTo(comment1.getId());
        assertThat(updateComment.getPost()).isEqualTo(comment1.getPost());
        assertThat(updateComment.getWriter()).isEqualTo(comment1.getWriter());
        assertThat(updateComment.getContent()).isEqualTo("수정된 댓글");
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteById() {
        // given

        // when
        commentService.deleteById(post1.getId(), comment1.getId(), user1.getId());

        // then
        assertThatThrownBy(() -> commentService.findById(comment1.getId()))
                .isInstanceOf(CommunityCommentException.class);
        assertThat(commentService.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 개수 - 해당 게시글")
    void countByPostId() {
        // given

        // when
        Long count = commentService.countByPostId(post1.getId());

        // then
        assertThat(count).isEqualTo(1);
    }
}
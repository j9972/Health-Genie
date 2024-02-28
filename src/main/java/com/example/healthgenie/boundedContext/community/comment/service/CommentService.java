package com.example.healthgenie.boundedContext.community.comment.service;

import com.example.healthgenie.base.exception.CommunityCommentException;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.comment.repository.CommentRepository;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.CommunityCommentErrorResult.COMMENT_EMPTY;
import static com.example.healthgenie.base.exception.CommunityCommentErrorResult.NO_PERMISSION;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public Comment save(Long postId, Long userId, CommentRequest request) {
        Post post = postService.findById(postId);

        User writer = userService.findById(userId);

        Comment comment = Comment.builder()
                .post(post)
                .commentBody(request.getContent())
                .writer(writer)
                .build();

        commentRepository.save(comment);

        return comment;
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostIdOrderByIdDesc(postId);
    }

    @Transactional
    public Comment update(Long postId, Long commentId, Long userId, CommentRequest request) {
        postService.findById(postId);

        Comment comment = findById(commentId);

        if(!Objects.equals(userId, comment.getWriter().getId())) {
            throw new CommunityCommentException(NO_PERMISSION);
        }

        if(StringUtils.hasText(request.getContent())) {
            comment.updateContent(request.getContent());
        }

        return comment;
    }

    @Transactional
    public String deleteById(Long postId, Long commentId, Long userId) {
        postService.findById(postId);

        Comment comment = findById(commentId);

        if(!Objects.equals(userId, comment.getWriter().getId())) {
            throw new CommunityCommentException(NO_PERMISSION);
        }

        commentRepository.deleteById(commentId);

        return "댓글이 삭제 되었습니다. [id=" + commentId + "]";
    }

}

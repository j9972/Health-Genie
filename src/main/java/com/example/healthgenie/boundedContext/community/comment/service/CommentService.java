package com.example.healthgenie.boundedContext.community.comment.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.comment.repository.CommentQueryRepository;
import com.example.healthgenie.boundedContext.community.comment.repository.CommentRepository;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final PostService postService;

    @Transactional
    public Comment save(Long postId, User writer, CommentRequest request) {
        Post post = postService.findById(postId);

        Comment comment = Comment.builder()
                .post(post)
                .content(request.getContent())
                .writer(writer)
                .build();

        return commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND, "commentId="+commentId));
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostIdOrderByIdDesc(postId);
    }

    public List<CommentResponse> findDtosAllByPostId(Long postId) {
        return commentQueryRepository.findDtosAllByPostId(postId);
    }

    @Transactional
    public Comment update(Long postId, Long commentId, Long userId, CommentRequest request) {
        postService.findById(postId);

        Comment comment = findById(commentId);

        if (!Objects.equals(userId, comment.getWriter().getId())) {
            throw new CustomException(NO_PERMISSION);
        }

        if (StringUtils.hasText(request.getContent())) {
            comment.updateContent(request.getContent());
        }

        return comment;
    }

    @Transactional
    public String deleteById(Long postId, Long commentId, Long userId) {
        postService.findById(postId);

        Comment comment = findById(commentId);

        if (!Objects.equals(userId, comment.getWriter().getId())) {
            throw new CustomException(NO_PERMISSION);
        }

        commentRepository.deleteById(commentId);

        return "댓글이 삭제 되었습니다. [id=" + commentId + "]";
    }

    public Long countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}

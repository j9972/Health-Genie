package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CommunityCommentException;
import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.community.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.entity.Comment;
import com.example.healthgenie.boundedContext.community.entity.Post;
import com.example.healthgenie.boundedContext.community.repository.CommentRepository;
import com.example.healthgenie.boundedContext.community.repository.PostRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.healthgenie.base.exception.CommunityCommentErrorResult.COMMENT_EMPTY;
import static com.example.healthgenie.base.exception.CommunityCommentErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponse save(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .post(post)
                .commentBody(request.getContent())
                .writer(writer)
                .build();

        commentRepository.save(comment);

        post.addComment(comment);

        return CommentResponse.of(comment);
    }

    public CommentResponse findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));

        return CommentResponse.of(comment);
    }

    public List<CommentResponse> findAll() {
        return CommentResponse.of(commentRepository.findAll());
    }

    public List<CommentResponse> findAllByPostId(Long postId) {
        return CommentResponse.of(commentRepository.findAllByPostIdOrderByIdDesc(postId));
    }

    public CommentResponse update(Long postId, Long commentId, Long userId, CommentRequest request) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));

        if(!comment.getWriter().getId().equals(userId)) {
            throw new CommunityCommentException(NO_PERMISSION);
        }

        if(StringUtils.hasText(request.getContent())) {
            comment.changeContent(request.getContent());
        }

        return CommentResponse.of(comment);
    }

    public void deleteById(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));

        if(!comment.getWriter().getId().equals(userId)) {
            throw new CommunityCommentException(NO_PERMISSION);
        }

        commentRepository.deleteById(commentId);

        post.removeComment(comment);
    }

}

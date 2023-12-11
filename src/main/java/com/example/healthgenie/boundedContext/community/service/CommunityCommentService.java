package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CommunityCommentException;
import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.repository.CommunityCommentRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.CommunityCommentErrorResult.COMMENT_EMPTY;
import static com.example.healthgenie.base.exception.CommunityCommentErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityPostRepository communityPostRepository;
    private final UserRepository userRepository;

    public CommentResponse save(Long postId, CommentRequest request) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        User writer = userRepository.findById(request.getWriterId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        CommunityComment comment = CommunityComment.builder()
                .post(post)
                .commentBody(request.getContent())
                .writer(writer)
                .build();

        communityCommentRepository.save(comment);

        post.addComment(comment);

        return CommentResponse.of(comment);
    }

    public CommentResponse findById(Long id) {
        CommunityComment comment = communityCommentRepository.findById(id)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));

        return CommentResponse.of(comment);
    }

    public List<CommentResponse> findAll() {
        return CommentResponse.of(communityCommentRepository.findAll());
    }

    public List<CommentResponse> findAllByPostId(Long postId) {
        return CommentResponse.of(communityCommentRepository.findAllByPostIdOrderByIdDesc(postId));
    }

    public CommentResponse update(Long postId, Long commentId, CommentRequest request) {
        communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        CommunityComment comment = communityCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));

        if(!comment.getWriter().getId().equals(request.getWriterId())) {
            throw new CommunityCommentException(NO_PERMISSION);
        }

        if(request.getContent() != null) {
            comment.changeContent(request.getContent());
        }

        return CommentResponse.of(comment);
    }

    public void deleteById(Long postId, Long commentId) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        CommunityComment comment = communityCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommunityCommentException(COMMENT_EMPTY));

        User user = SecurityUtils.getCurrentUser();

        if(!comment.getWriter().getId().equals(user.getId())) {
            throw new CommunityCommentException(NO_PERMISSION);
        }

        communityCommentRepository.deleteById(commentId);

        post.removeComment(comment);
    }

}

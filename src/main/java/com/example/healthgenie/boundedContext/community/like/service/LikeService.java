package com.example.healthgenie.boundedContext.community.like.service;

import com.example.healthgenie.base.exception.LikeException;
import com.example.healthgenie.boundedContext.community.like.entity.Like;
import com.example.healthgenie.boundedContext.community.like.repository.LikeRepository;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.LikeErrorResult.ALREADY_LIKED;
import static com.example.healthgenie.base.exception.LikeErrorResult.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public Like like(Long postId, Long userId) {
        likeRepository.findByPostIdAndUserId(postId, userId).ifPresent(like -> {
            throw new LikeException(ALREADY_LIKED);
        });

        Post post = postService.findById(postId);
        User user = userService.findById(userId);

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        return likeRepository.save(like);
    }

    @Transactional
    public String cancel(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new LikeException(NOT_FOUND));

        likeRepository.delete(like);

        return postId + "번 게시물의 좋아요가 취소 되었습니다.";
    }

    public List<Like> findAllByPostId(Long postId) {
        return likeRepository.findAllByPostId(postId);
    }
}


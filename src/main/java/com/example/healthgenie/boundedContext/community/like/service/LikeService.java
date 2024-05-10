package com.example.healthgenie.boundedContext.community.like.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.community.like.dto.LikeRequest;
import com.example.healthgenie.boundedContext.community.like.entity.Like;
import com.example.healthgenie.boundedContext.community.like.repository.LikeRepository;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.DUPLICATED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;

    @Transactional
    public Like save(Long postId, LikeRequest request, User user) {
        likeRepository.findByPostIdAndUserId(postId, request.getUserId()).ifPresent(like -> {
            throw new CustomException(DUPLICATED, "이미 좋아요를 표시했습니다.");
        });

        Post post = postService.findById(postId);

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        return likeRepository.save(like);
    }

    @Transactional
    public String delete(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND, "postId="+postId+"&userId="+userId));

        likeRepository.delete(like);

        return postId + "번 게시물의 좋아요가 취소 되었습니다.";
    }

    public List<Like> findAllByPostId(Long postId) {
        return likeRepository.findAllByPostId(postId);
    }

    public Long countByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public Boolean existsByPostIdAndUserId(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }
}


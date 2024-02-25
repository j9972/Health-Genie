package com.example.healthgenie.boundedContext.community.service;


import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostQueryRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final CommunityPostQueryRepository communityPostQueryRepository;
    private final UserRepository userRepository;

    public PostResponse findById(Long postId) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        return PostResponse.of(post);
    }

    public List<PostResponse> findAll(String keyword) {
        return PostResponse.excludePhotosAndCommentsOf(communityPostQueryRepository.findAll(keyword));
    }

    @Transactional
    public PostResponse save(Long userId, PostRequest request) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        CommunityPost savedPost = communityPostRepository.save(CommunityPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(writer)
                .build());

        return PostResponse.of(savedPost);
    }

    @Transactional
    public String delete(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(currentUser.getId(), post.getWriter().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        communityPostRepository.delete(post);

        return "게시글이 삭제 되었습니다.";
    }

    @Transactional
    public PostResponse update(Long postId, Long userId, PostRequest request) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(userId, post.getWriter().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        if(request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }

        if(request.getContent() != null) {
            post.updateContent(request.getContent());
        }

        return PostResponse.of(post);
    }
}
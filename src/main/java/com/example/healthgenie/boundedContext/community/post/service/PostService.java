package com.example.healthgenie.boundedContext.community.post.service;


import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.post.repository.PostQueryRepository;
import com.example.healthgenie.boundedContext.community.post.repository.PostRepository;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
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
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserRepository userRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));
    }

    public List<PostResponse> findAll(String keyword) {
        return PostResponse.excludePhotosAndCommentsOf(postQueryRepository.findAll(keyword));
    }

    @Transactional
    public PostResponse save(Long userId, PostRequest request) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Post savedPost = postRepository.save(Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(writer)
                .build());

        return PostResponse.of(savedPost);
    }

    @Transactional
    public String delete(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(currentUser.getId(), post.getWriter().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        postRepository.delete(post);

        return "게시글이 삭제 되었습니다.";
    }

    @Transactional
    public PostResponse update(Long postId, Long userId, PostRequest request) {
        Post post = postRepository.findById(postId)
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
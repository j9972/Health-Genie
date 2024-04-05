package com.example.healthgenie.boundedContext.community.post.service;


import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.repository.PostQueryRepository;
import com.example.healthgenie.boundedContext.community.post.repository.PostRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserService userService;

    public PostResponse findDtoById(Long postId) {
        return postQueryRepository.findDtoById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND, "postId="+postId));
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND, "postId="+postId));
    }

    public Slice<Post> findAll(String keyword, Long userId, Long lastId, Pageable pageable) {
        return postQueryRepository.findAll(keyword, userId, lastId, pageable);
    }

    @Transactional
    public Post save(Long userId, PostRequest request) {
        User writer = userService.findById(userId);

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(writer)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public Post update(Long postId, Long userId, PostRequest request) {
        Post post = findById(postId);

        if (!Objects.equals(userId, post.getWriter().getId())) {
            throw new CustomException(NO_PERMISSION);
        }

        if (StringUtils.hasText(request.getTitle())) {
            post.updateTitle(request.getTitle());
        }

        if (StringUtils.hasText(request.getContent())) {
            post.updateContent(request.getContent());
        }

        return post;
    }

    @Transactional
    public String delete(Long postId, Long userId) {
        Post post = findById(postId);

        if (!Objects.equals(userId, post.getWriter().getId())) {
            throw new CustomException(NO_PERMISSION);
        }

        postRepository.delete(post);

        return "게시글이 삭제 되었습니다.";
    }
}
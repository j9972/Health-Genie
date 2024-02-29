package com.example.healthgenie.boundedContext.community.post.service;


import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.repository.PostQueryRepository;
import com.example.healthgenie.boundedContext.community.post.repository.PostRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    public Slice<Post> findAll(String keyword, Long lastId, Pageable pageable) {
        return postQueryRepository.findAll(keyword, lastId, pageable);
    }

    @Transactional
    public Post save(Long userId, PostRequest request) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

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

        if(!Objects.equals(userId, post.getWriter().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        if(StringUtils.hasText(request.getTitle())) {
            post.updateTitle(request.getTitle());
        }

        if(StringUtils.hasText(request.getContent())) {
            post.updateContent(request.getContent());
        }

        return post;
    }

    @Transactional
    public String delete(Long postId, Long userId) {
        Post post = findById(postId);

        if(!Objects.equals(userId, post.getWriter().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        postRepository.delete(post);

        return "게시글이 삭제 되었습니다.";
    }
}
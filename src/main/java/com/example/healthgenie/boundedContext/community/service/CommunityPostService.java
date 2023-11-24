package com.example.healthgenie.boundedContext.community.service;


import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostQueryRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final CommunityPostQueryRepository communityPostQueryRepository;
    private final CommunityPostPhotoService communityPostPhotoService;

//    public List<PostResponse> findAll() {
//        return communityPostRepository.findAll().stream()
//                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getMember().getId(), p.getCommunityPostPhotos()))
//                .collect(Collectors.toList());
//    }

    public PostResponse findDtoById(Long id) {
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        return PostResponse.of(post);
    }

    public CommunityPost findById(Long id) {
        return communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));
    }

    @Transactional
    public PostResponse save(PostRequest dto) {
        User currentUser = SecurityUtils.getCurrentUser();

        CommunityPost savedPost = communityPostRepository.save(CommunityPost.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(currentUser)
                .build());

        return PostResponse.of(savedPost);
    }

    @Transactional
    public String delete(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(currentUser.getId(), post.getMember().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        communityPostRepository.delete(post);

        return "게시글이 삭제 되었습니다.";
    }

    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(currentUser.getId(), post.getMember().getId())) {
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

    public List<PostResponse> findAll(String keyword) {
        return PostResponse.of(communityPostQueryRepository.findAll(keyword));
    }
}
package com.example.healthgenie.service;


import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.domain.community.entity.CommunityPostPhoto;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.global.config.SecurityUtil;
import com.example.healthgenie.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.exception.CommunityPostErrorResult.NO_PERMISSION;
import static com.example.healthgenie.exception.CommunityPostErrorResult.POST_EMPTY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final CommunityPostPhotoService communityPostPhotoService;
    private final S3UploadService s3UploadService;

//    public List<PostResponse> findAll() {
//        return communityPostRepository.findAll().stream()
//                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getMember().getId(), p.getCommunityPostPhotos()))
//                .collect(Collectors.toList());
//    }

    public PostResponse findById(Long id) {
        User currentUser = SecurityUtil.getCurrentUser();

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        List<String> photoPaths = post.getCommunityPostPhotos().stream()
                .map(CommunityPostPhoto::getPostPhotoPath)
                .toList();

        return PostResponse.builder()
                .id(id)
                .title(post.getTitle())
                .content(post.getContent())
                .userId(currentUser.getId())
                .photoPaths(photoPaths)
                .build();
    }

    @Transactional
    public PostResponse save(PostRequest dto) {
        User currentUser = SecurityUtil.getCurrentUser();

        CommunityPost savedPost = communityPostRepository.save(CommunityPost.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(currentUser)
                .build());

        return PostResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .userId(currentUser.getId())
                .build();
    }

    @Transactional
    public void delete(Long id) {
        User currentUser = SecurityUtil.getCurrentUser();

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(currentUser.getId(), post.getMember().getId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        communityPostRepository.delete(post);
    }

    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }

        if(request.getContent() != null) {
            post.updateContent(request.getContent());
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(currentUser.getId())
                .build();
    }
}
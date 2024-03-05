package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostPhotoRepository;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.PHOTO_EMPTY;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostPhotoService {

    private final CommunityPostPhotoRepository communityPostPhotoRepository;
    private final CommunityPostRepository communityPostRepository;

    @Transactional
    public CommunityPostPhoto save(Long postId, String path) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        CommunityPostPhoto photo = CommunityPostPhoto.builder()
                .postPhotoPath(path)
                .post(post)
                .build();

        return communityPostPhotoRepository.save(photo);
    }

    @Transactional
    public List<CommunityPostPhoto> saveAll(Long postId, List<String> photoPaths) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        List<CommunityPostPhoto> photos = photoPaths.stream()
                .map(path -> CommunityPostPhoto.builder()
                        .postPhotoPath(path)
                        .post(post)
                        .build())
                .toList();

        // 객체 그래프 탐색용
        for (CommunityPostPhoto photo : photos) {
            post.addPhoto(photo);
        }

        return communityPostPhotoRepository.saveAll(photos);
    }

    public CommunityPostPhoto findById(Long id) {
        return communityPostPhotoRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(PHOTO_EMPTY));
    }

    public List<CommunityPostPhoto> findAll() {
        return communityPostPhotoRepository.findAll();
    }

    @Transactional
    public List<CommunityPostPhoto> updateAll(Long postId, List<String> photoPaths) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        // 객체 그래프 탐색용
        post.removePhotos(post.getCommunityPostPhotos());

        // 기존 Photo 삭제
        communityPostPhotoRepository.deleteAllByPostId(postId);

        // 새로운 Photo 저장
        return saveAll(postId, photoPaths);
    }
}

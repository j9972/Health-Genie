package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 작업 단위를 늘려서 같은 트랜잭션에서 변경이 일어나게 하기 위한 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityPostTransactionService {

    private final S3UploadUtils s3UploadUtils;
    private final CommunityPostService communityPostService;
    private final CommunityPostPhotoService communityPostPhotoService;

    public PostResponse save(PostRequest request) throws IOException {
        List<String> photoPaths = new ArrayList<>();
        PostResponse savedPost = null;
        try {
            // 이미지 S3 저장
            if (existsFile(request)) {
                photoPaths = s3UploadUtils.upload(request.getPhotos(), "post-photos");
            }

            // CommunityPost 엔티티 저장
            savedPost = communityPostService.save(request);

            // CommunityPostPhoto 엔티티 저장
            if (existsFile(request)) {
                communityPostPhotoService.saveAll(savedPost.getId(), photoPaths);
            }
        } catch (Exception e) {
            for(String fileUrl : photoPaths) {
                s3UploadUtils.deleteS3Object("post-photos", fileUrl);
            }
            throw e;
        }

        return PostResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .userId(savedPost.getUserId())
                .photoPaths(photoPaths)
                .build();
    }

    public PostResponse update(Long id, PostRequest request) throws IOException {
        List<String> photoPaths = new ArrayList<>();
        PostResponse updatedPost = null;
        try {
            // 이미지 S3 저장
            if (existsFile(request)) {
                photoPaths = s3UploadUtils.upload(request.getPhotos(), "post-photos");
            }

            // CommunityPost 엔티티 저장
            updatedPost = communityPostService.update(id, request);

            // CommunityPostPhoto 엔티티 저장
            if (existsFile(request)) {
                communityPostPhotoService.updateAll(updatedPost.getId(), photoPaths);
            }
        } catch (Exception e) {
            for(String fileUrl : photoPaths) {
                s3UploadUtils.deleteS3Object("post-photos", fileUrl);
            }
            throw e;
        }

        return PostResponse.builder()
                .id(updatedPost.getId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .userId(updatedPost.getUserId())
                .photoPaths(photoPaths)
                .build();
    }

    private boolean existsFile(PostRequest request) {
        long totalFileSize = request.getPhotos().stream()
                .mapToLong(MultipartFile::getSize)
                .sum();

        return !request.getPhotos().isEmpty() && totalFileSize > 0;
    }
}

package com.example.healthgenie.boundedContext.community.service;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;

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
    private final CommunityPostRepository communityPostRepository;
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
                .date(savedPost.getDate())
                .time(savedPost.getTime())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .writer(savedPost.getWriter())
                .photoPaths(photoPaths)
                .build();
    }

    public PostResponse update(Long postId, PostRequest request) throws IOException {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(!Objects.equals(post.getWriter().getId(), SecurityUtils.getCurrentUserId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        List<String> photoPaths = new ArrayList<>();
        PostResponse updatedPost = null;
        try {
            // 이미지 S3 저장
            if (existsFile(request)) {
                List<String> removePaths = post.getCommunityPostPhotos().stream()
                        .map(CommunityPostPhoto::getPostPhotoPath)
                        .toList();

                for(String fileUrl : removePaths) {
                    s3UploadUtils.deleteS3Object("post-photos", fileUrl);
                }

                photoPaths = s3UploadUtils.upload(request.getPhotos(), "post-photos");
            }

            // CommunityPost 엔티티 저장
            updatedPost = communityPostService.update(postId, request);

            // CommunityPostPhoto 엔티티 저장
            if (existsFile(request)) {
                communityPostPhotoService.updateAll(updatedPost.getId(), photoPaths);
            } else {
                updatedPost.setPhotoPaths(new ArrayList<>());
            }
        } catch (Exception e) {
            for(String fileUrl : photoPaths) {
                s3UploadUtils.deleteS3Object("post-photos", fileUrl);
            }
            throw e;
        }

        LocalDateTime dateTime = post.getCreatedDate();
        String date = DateUtils.toDate(dateTime);
        String time = DateUtils.toTime(dateTime);

        return PostResponse.builder()
                .id(post.getId())
                .date(date)
                .time(time)
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .writer(updatedPost.getWriter())
                .photoPaths(updatedPost.getPhotoPaths())
                .build();
    }

    private boolean existsFile(PostRequest request) {
        long totalFileSize = request.getPhotos().stream()
                .mapToLong(MultipartFile::getSize)
                .sum();

        return !request.getPhotos().isEmpty() && totalFileSize > 0;
    }
}

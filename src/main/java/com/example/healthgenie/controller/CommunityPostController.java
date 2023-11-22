package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.global.config.SecurityUtil;
import com.example.healthgenie.service.CommunityPostPhotoService;
import com.example.healthgenie.service.CommunityPostService;
import com.example.healthgenie.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.exception.CommunityPostErrorResult.NO_PERMISSION;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/post")
public class CommunityPostController {

    private final CommunityPostService communityPostService;
    private final CommunityPostPhotoService communityPostPhotoService;
    private final S3UploadService s3UploadService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(communityPostService.findById(id));
    }

    @PostMapping("/write")
    public ResponseEntity<PostResponse> save(PostRequest request) throws IOException {
        // 이미지 S3 저장
        List<String> photoPaths = new ArrayList<>();
        if(existsFile(request)) {
            photoPaths = s3UploadService.upload(request.getPhotos(), "post-photos");
        }

        // CommunityPost 엔티티 저장
        PostResponse savedPost = communityPostService.save(request);

        // CommunityPostPhoto 엔티티 저장
        if(existsFile(request)) {
            communityPostPhotoService.saveAll(savedPost.getId(), photoPaths);
        }

        PostResponse response = PostResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .userId(savedPost.getUserId())
                .photoPaths(photoPaths)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<PostResponse> edit(@PathVariable Long id, PostRequest request) throws IOException {
        PostResponse post = communityPostService.findById(id);
        if(!Objects.equals(post.getUserId(), SecurityUtil.getCurrentUserId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        // 이미지 S3 저장
        List<String> photoPaths = new ArrayList<>();
        if(existsFile(request)) {
            photoPaths = s3UploadService.upload(request.getPhotos(), "post-photos");
        }

        // CommunityPost 엔티티 저장
        PostResponse updatedPost = communityPostService.update(id, request);

        // CommunityPostPhoto 엔티티 저장
        if (existsFile(request)) {
            communityPostPhotoService.updateAll(updatedPost.getId(), photoPaths);
        }

        PostResponse response = PostResponse.builder()
                .id(updatedPost.getId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .userId(updatedPost.getUserId())
                .photoPaths(photoPaths)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        PostResponse post = communityPostService.findById(id);
        if(!Objects.equals(post.getUserId(), SecurityUtil.getCurrentUserId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        communityPostService.delete(id);

        return ResponseEntity.ok(id + "번 게시글이 삭제되었습니다.");
    }

    private boolean existsFile(PostRequest request) {
        long totalFileSize = request.getPhotos().stream()
                .mapToLong(MultipartFile::getSize)
                .sum();

        return !request.getPhotos().isEmpty() && totalFileSize > 0;
    }

    @GetMapping("/test/findAll")
    public List<PostResponse> findAll() {
        return communityPostService.findAll();
    }
}
package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.domain.community.entity.CommunityPostPhoto;
import com.example.healthgenie.service.CommunityPostPhotoService;
import com.example.healthgenie.service.CommunityPostService;
import com.example.healthgenie.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        if(!request.getPhotos().isEmpty()) {
            photoPaths = s3UploadService.upload(request.getPhotos(), "post-photos");
        }

        // CommunityPost 엔티티 저장
        PostResponse savedPost = communityPostService.save(request);

        // CommunityPostPhoto 엔티티 저장
        List<CommunityPostPhoto> savedPhotos = communityPostPhotoService.saveAll(savedPost.getId(), photoPaths);

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
    public ResponseEntity<PostResponse> edit(@PathVariable Long id, @RequestBody PostRequest request) {
        return ResponseEntity.ok(communityPostService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        communityPostService.delete(id);
        return ResponseEntity.ok(id + "번 게시글이 삭제되었습니다.");
    }
}
package com.example.healthgenie.boundedContext.community.photo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.photo.dto.PhotoRequest;
import com.example.healthgenie.boundedContext.community.photo.dto.PhotoResponse;
import com.example.healthgenie.boundedContext.community.photo.service.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts/{postId}/photos")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<Result> save(@PathVariable Long postId, @Valid PhotoRequest request) throws IOException {
        List<PhotoResponse> response = PhotoResponse.of(photoService.save(postId, request));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<Result> findById(@PathVariable Long postId, @PathVariable Long photoId) {
        PhotoResponse response = PhotoResponse.of(photoService.findById(photoId));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(@PathVariable Long postId) {
        List<PhotoResponse> response = PhotoResponse.of(photoService.findAllByPostId(postId));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping
    public ResponseEntity<Result> delete(@PathVariable Long postId) {
        String response = photoService.deleteAllByPostId(postId);

        return ResponseEntity.ok(Result.of(response));
    }
}

package com.example.healthgenie.boundedContext.community.photo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.photo.dto.PhotoRequest;
import com.example.healthgenie.boundedContext.community.photo.dto.PhotoResponse;
import com.example.healthgenie.boundedContext.community.photo.service.PhotoService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts/{postId}/photos")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<Result> save(@PathVariable Long postId, @AuthenticationPrincipal User user,
                                       @Valid PhotoRequest request) throws IOException {
        List<PhotoResponse> response = PhotoResponse.of(photoService.save(postId, user.getId(), request));

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
    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        String response = photoService.deleteAllByPostId(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}
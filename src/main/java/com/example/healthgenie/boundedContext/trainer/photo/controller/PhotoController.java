package com.example.healthgenie.boundedContext.trainer.photo.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.trainer.photo.dto.PhotoRequest;
import com.example.healthgenie.boundedContext.trainer.photo.dto.PhotoResponse;
import com.example.healthgenie.boundedContext.trainer.photo.service.TrainerProfilePhotoService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainers/profiles/{profileId}/photos")
public class PhotoController {

    private final TrainerProfilePhotoService trainerProfilePhotoService;

    @PostMapping
    public ResponseEntity<Result> save(@PathVariable Long postId, @AuthenticationPrincipal User user,
                                       @Valid PhotoRequest request) throws IOException {
        List<PhotoResponse> response = PhotoResponse.of(trainerProfilePhotoService.save(postId, user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<Result> findById(@PathVariable Long postId, @PathVariable Long photoId) {
        PhotoResponse response = PhotoResponse.of(trainerProfilePhotoService.findById(photoId));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(@PathVariable Long postId) {
        List<PhotoResponse> response = PhotoResponse.of(trainerProfilePhotoService.findAllByPostId(postId));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping
    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        String response = trainerProfilePhotoService.deleteAllByPostId(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}

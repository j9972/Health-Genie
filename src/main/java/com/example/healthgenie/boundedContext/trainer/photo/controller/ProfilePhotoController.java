package com.example.healthgenie.boundedContext.trainer.photo.controller;

import com.example.healthgenie.boundedContext.trainer.photo.service.ProfilePhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainers/profiles/{profileId}/photos")
public class ProfilePhotoController {

    private final ProfilePhotoService photoService;

//    @PostMapping(value = "/{profileId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<Result> save(@PathVariable Long profileId, @AuthenticationPrincipal User user,
//                                       @RequestPart(name = "profileImages", required = false) ProfilePhotoRequest photos) throws IOException {
//        List<PhotoResponse> response = PhotoResponse.of(photoService.save(profileId, user.getId(), photos));
//
//        return ResponseEntity.ok(Result.of(response));
//    }
//
//    @GetMapping("/{photoId}")
//    public ResponseEntity<Result> findById(@PathVariable Long postId, @PathVariable Long photoId) {
//        PhotoResponse response = PhotoResponse.of(photoService.findById(photoId));
//
//        return ResponseEntity.ok(Result.of(response));
//    }
//
//    @GetMapping
//    public ResponseEntity<Result> findAll(@PathVariable Long postId) {
//        List<PhotoResponse> response = PhotoResponse.of(photoService.findAllByPostId(postId));
//
//        return ResponseEntity.ok(Result.of(response));
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) {
//        String response = photoService.deleteAllByPostId(postId, user.getId());
//
//        return ResponseEntity.ok(Result.of(response));
//    }
}

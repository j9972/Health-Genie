package com.example.healthgenie.boundedContext.community.like.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.like.dto.LikeResponse;
import com.example.healthgenie.boundedContext.community.like.service.LikeService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Result> save(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        LikeResponse response = LikeResponse.of(likeService.save(postId, user));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/count")
    public ResponseEntity<Result> count(@PathVariable Long postId) {
        return ResponseEntity.ok(Result.of(likeService.countByPostId(postId)));
    }

    @DeleteMapping
    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        String response = likeService.delete(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}

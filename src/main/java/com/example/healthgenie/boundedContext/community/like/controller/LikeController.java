package com.example.healthgenie.boundedContext.community.like.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.like.dto.LikeResponse;
import com.example.healthgenie.boundedContext.community.like.entity.Like;
import com.example.healthgenie.boundedContext.community.like.service.LikeService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes/{postId}")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Result> like(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        Like like = likeService.like(postId, user.getId());

        LikeResponse response = LikeResponse.of(like);

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping
    public ResponseEntity<Result> cancel(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        String response = likeService.cancel(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}

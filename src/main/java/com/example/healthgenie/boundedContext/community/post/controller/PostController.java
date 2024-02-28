package com.example.healthgenie.boundedContext.community.post.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.community.post.service.PostTransactionService;
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
@RequestMapping("/community/posts")
public class PostController {

    private final PostService postService;
    private final PostTransactionService postTransactionService;

    @GetMapping("/test")
    public ResponseEntity<Result> findAll(@RequestParam(name = "search", defaultValue = "") String keyword) {
        List<PostResponse> response = PostResponse.of(postService.findAll(keyword));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Result> findById(@PathVariable Long postId) {
        PostResponse response = PostResponse.of(postService.findById(postId));

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping
    public ResponseEntity<Result> save(@AuthenticationPrincipal User user, @Valid PostRequest request) throws IOException {
        PostResponse response = PostResponse.of(postService.save(user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Result> update(@PathVariable Long postId, @AuthenticationPrincipal User user, @Valid PostRequest request) throws IOException {
        PostResponse response = PostResponse.of(postService.update(postId, user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) throws IOException {
        String response = postService.delete(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}
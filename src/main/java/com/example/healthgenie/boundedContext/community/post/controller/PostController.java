package com.example.healthgenie.boundedContext.community.post.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.post.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.post.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.post.dto.PostSliceResponse;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Result> findAll(@RequestParam(name = "search", defaultValue = "") String keyword,
                                          @RequestParam(name = "userId", required = false) Long userId,
                                          @RequestParam(name = "lastId", required = false) Long lastId,
                                          Pageable pageable) {
        PostSliceResponse response = PostSliceResponse.of(postService.findAll(keyword, userId, lastId, pageable));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Result> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(Result.of(postService.findDtoById(postId)));
    }

    @PostMapping
    public ResponseEntity<Result> save(@AuthenticationPrincipal User user, @RequestBody @Valid PostRequest request) throws IOException {
        PostResponse response = PostResponse.of(postService.save(user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Result> update(@PathVariable Long postId, @AuthenticationPrincipal User user, @RequestBody @Valid PostRequest request) throws IOException {
        PostResponse response = PostResponse.of(postService.update(postId, user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) throws IOException {
        String response = postService.delete(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}
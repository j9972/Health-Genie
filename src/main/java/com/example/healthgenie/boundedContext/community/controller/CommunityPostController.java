package com.example.healthgenie.boundedContext.community.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.service.CommunityPostService;
import com.example.healthgenie.boundedContext.community.service.CommunityPostTransactionService;
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
public class CommunityPostController {

    private final CommunityPostService communityPostService;
    private final CommunityPostTransactionService communityPostTransactionService;

    @GetMapping
    public ResponseEntity<Result> findAll(@RequestParam(name = "search", defaultValue = "") String keyword) {
        List<PostResponse> response = communityPostService.findAll(keyword);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Result> findById(@PathVariable Long postId) {
        PostResponse response = communityPostService.findById(postId);

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping
    public ResponseEntity<Result> save(@AuthenticationPrincipal User user, @Valid PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.save(user.getId(), request);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Result> edit(@PathVariable Long postId, @AuthenticationPrincipal User user, PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.update(postId, user.getId(), request);

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Result> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) throws IOException {
        String response = communityPostTransactionService.delete(postId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}
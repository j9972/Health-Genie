package com.example.healthgenie.boundedContext.community.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.service.CommunityPostService;
import com.example.healthgenie.boundedContext.community.service.CommunityPostTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<Result> findById(@PathVariable Long id) {
        PostResponse response = communityPostService.findById(id);

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping
    public ResponseEntity<Result> save(PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.save(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.update(id, request);

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        String response = communityPostService.delete(id);

        return ResponseEntity.ok(Result.of(response));
    }
}
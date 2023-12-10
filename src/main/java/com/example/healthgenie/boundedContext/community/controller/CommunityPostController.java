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

    /*
    TODO : 게시글이 삭제됐을 때, 사진이 있다면 s3에서도 삭제되어야 함
     */
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
    public ResponseEntity<Result> save(PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.save(request);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Result> edit(@PathVariable Long postId, PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.update(postId, request);

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Result> delete(@PathVariable Long postId) {
        String response = communityPostService.delete(postId);

        return ResponseEntity.ok(Result.of(response));
    }
}
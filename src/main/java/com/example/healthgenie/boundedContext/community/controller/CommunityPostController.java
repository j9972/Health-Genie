package com.example.healthgenie.boundedContext.community.controller;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.utils.SecurityUtils;
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
import java.util.Objects;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.NO_PERMISSION;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class CommunityPostController {

    private final CommunityPostService communityPostService;
    private final CommunityPostTransactionService communityPostTransactionService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(communityPostService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PostResponse> save(PostRequest request) throws IOException {
        PostResponse response = communityPostTransactionService.save(request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> edit(@PathVariable Long id, PostRequest request) throws IOException {
        PostResponse post = communityPostService.findById(id);
        if(!Objects.equals(post.getUserId(), SecurityUtils.getCurrentUserId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        PostResponse response = communityPostTransactionService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        PostResponse post = communityPostService.findById(id);
        if(!Objects.equals(post.getUserId(), SecurityUtils.getCurrentUserId())) {
            throw new CommunityPostException(NO_PERMISSION);
        }

        communityPostService.delete(id);

        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @GetMapping("/test/findAll")
    public List<PostResponse> findAll() {
        return communityPostService.findAll();
    }
}
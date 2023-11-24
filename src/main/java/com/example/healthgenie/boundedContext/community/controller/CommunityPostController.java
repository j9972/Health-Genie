package com.example.healthgenie.boundedContext.community.controller;

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

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(communityPostService.findDtoById(id));
    }

    @PostMapping
    public ResponseEntity<PostResponse> save(PostRequest request) throws IOException {
        return ResponseEntity.ok(communityPostTransactionService.save(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> edit(@PathVariable Long id, PostRequest request) throws IOException {
        return ResponseEntity.ok(communityPostTransactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(communityPostService.delete(id));
    }

    @GetMapping("/test/findAll")
    public List<PostResponse> findAll(@RequestParam(name = "search", defaultValue = "") String keyword) {
        return communityPostService.findAll(keyword);
    }
}
package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/post")
public class CommunityPostController {

    private final CommunityPostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping("/write")
    public ResponseEntity<PostResponse> save(@RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.save(request));
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<PostResponse> edit(@PathVariable Long id, @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(id + "번 게시글이 삭제되었습니다.");
    }
}
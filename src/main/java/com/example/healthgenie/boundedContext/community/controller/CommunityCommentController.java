package com.example.healthgenie.boundedContext.community.controller;

import com.example.healthgenie.boundedContext.community.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/community/post")
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> write(@PathVariable Long postId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(communityCommentService.save(postId, request));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> findAll(@PathVariable Long postId) {
        return ResponseEntity.ok(communityCommentService.findAllByPostId(postId));
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> edit(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(communityCommentService.update(postId, commentId, request));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> delete(@PathVariable Long commentId, @PathVariable Long postId) {
        communityCommentService.deleteById(postId, commentId);
        return ResponseEntity.ok("댓글이 삭제 되었습니다.");
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> findOne(@PathVariable String postId, @PathVariable Long commentId) {
        return ResponseEntity.ok(communityCommentService.findById(commentId));
    }
}

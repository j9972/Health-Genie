package com.example.healthgenie.boundedContext.community.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.service.CommentService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/community/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Result> write(@PathVariable Long postId, @AuthenticationPrincipal User user, CommentRequest request) {
        CommentResponse response = commentService.save(postId, user.getId(), request);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(@PathVariable Long postId) {
        List<CommentResponse> response = commentService.findAllByPostId(postId);

        return ResponseEntity.ok(Result.of(response));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Result> edit(@PathVariable Long postId,
                                       @PathVariable Long commentId,
                                       @AuthenticationPrincipal User user,
                                       CommentRequest request) {
        CommentResponse response = commentService.update(postId, commentId, user.getId(), request);

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Result> delete(@PathVariable Long commentId, @PathVariable Long postId, @AuthenticationPrincipal User user) {
        commentService.deleteById(postId, commentId, user.getId());

        return ResponseEntity.ok(Result.of("댓글이 삭제 되었습니다."));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Result> findOne(@PathVariable String postId, @PathVariable Long commentId) {
        CommentResponse response = commentService.findById(commentId);

        return ResponseEntity.ok(Result.of(response));
    }
}

package com.example.healthgenie.boundedContext.community.comment.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentRequest;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.comment.service.CommentService;
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
    public ResponseEntity<Result> save(@PathVariable Long postId, @AuthenticationPrincipal User user, @RequestBody CommentRequest request) {
        CommentResponse response = CommentResponse.of(commentService.save(postId, user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Result> findById(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentResponse response = CommentResponse.of(commentService.findById(commentId));

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping
    public ResponseEntity<Result> findAll(@PathVariable Long postId) {
        List<CommentResponse> response = commentService.findDtosAllByPostId(postId);

        return ResponseEntity.ok(Result.of(response));
    }

    @GetMapping("/count")
    public ResponseEntity<Result> count(@PathVariable Long postId) {
        return ResponseEntity.ok(Result.of(commentService.countByPostId(postId)));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Result> update(@PathVariable Long postId,
                                         @PathVariable Long commentId,
                                         @AuthenticationPrincipal User user,
                                         @RequestBody CommentRequest request) {
        CommentResponse response = CommentResponse.of(commentService.update(postId, commentId, user.getId(), request));

        return ResponseEntity.ok(Result.of(response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Result> delete(@PathVariable Long commentId, @PathVariable Long postId, @AuthenticationPrincipal User user) {
        String response = commentService.deleteById(postId, commentId, user.getId());

        return ResponseEntity.ok(Result.of(response));
    }
}

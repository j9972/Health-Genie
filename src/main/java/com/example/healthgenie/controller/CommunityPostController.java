package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/post")
public class CommunityPostController {

    private final CommunityPostService postService;

    @GetMapping
    public PostResponse findById(@RequestBody PostRequest request) {
        return postService.findById(request.getId());
    }

    @PostMapping("/write")
    public PostResponse save(@RequestBody PostRequest request) {
        return postService.save(request);
    }

    @PatchMapping("/edit/{id}")
    public Long edit(@PathVariable Long id, @RequestBody PostRequest request) {
        return postService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }
}
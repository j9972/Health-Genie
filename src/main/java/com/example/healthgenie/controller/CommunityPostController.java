package com.example.healthgenie.controller;

import com.example.healthgenie.dto.CommunitiyPostResponseDto;
import com.example.healthgenie.dto.CommunityPostGetResponseDto;
import com.example.healthgenie.dto.CommunityPostListResponseDto;
import com.example.healthgenie.dto.CommunityPostRequestDto;
import com.example.healthgenie.entity.CommunityPost;
import com.example.healthgenie.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommunityPostController {



    private final CommunityPostService postService;

    @PostMapping("/community/post/add")
    public ResponseEntity addPost(@RequestBody CommunityPostRequestDto dto){

        Long userId =1L; //로그인기능 완성전 임시 변수
        CommunitiyPostResponseDto result = postService.addPost(dto,userId);
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/community/post/get")
    public ResponseEntity getPost(@RequestParam Long postId){
        CommunityPostGetResponseDto result = postService.getPost(postId);

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/communtiy/post")
    public ResponseEntity getPostList(@RequestParam int pageNo){
        CommunityPostListResponseDto result = postService.getPostList(pageNo);

        return new ResponseEntity(result,HttpStatus.OK);
    }
}

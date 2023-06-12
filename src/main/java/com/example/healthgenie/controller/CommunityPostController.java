package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.CommunitiyPostResponseDto;
import com.example.healthgenie.domain.community.dto.CommunityPostGetResponseDto;
import com.example.healthgenie.domain.community.dto.CommunityPostIdTitleDto;
import com.example.healthgenie.domain.community.dto.CommunityPostRequestDto;
import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/post")
public class CommunityPostController {



    private final CommunityPostService postService;

    @PostMapping("/add")
    public ResponseEntity addPost(@RequestBody CommunityPostRequestDto dto){

        Long userId =1L; //로그인기능 완성전 임시 변수
        CommunitiyPostResponseDto result = postService.addPost(dto,userId);
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity getPost(@RequestParam Long postId){
        CommunityPostGetResponseDto result = postService.getPost(postId);

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<CommunityPostIdTitleDto>> getPostsByPage(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "20") int size) {

        Page<CommunityPost> posts = postService.getPostsByPage(page,size);
        Page<CommunityPostIdTitleDto> postDtos = posts.map(post -> new CommunityPostIdTitleDto(post.getId(), post.getTitle()));
        return new ResponseEntity(postDtos,HttpStatus.OK);
    }
}

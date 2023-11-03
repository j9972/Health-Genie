package com.example.healthgenie.controller;

import com.example.healthgenie.service.CommunityCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comment")
@Slf4j
public class CommunityCommentController {

    private final CommunityCommentService commentService;

    /*
    @PostMapping("/add")  // http://localhost:1234/api/v1/community/comment/add
    public ResponseEntity addComment(@RequestBody CommunityCommentRequestDto dto){

        Long postId =1L;
        Long userId =1L; // 로그인기능 완성전 임시 변수

        log.info("CommunityCommentController");

        CommunityCommentResponseDto result = commentService.addComment(dto, postId, userId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/get") // http://localhost:1234/api/v1/community/comment/get
    public ResponseEntity getComment(@RequestParam Long commentId){
        CommunityCommentGetResponseDto result = commentService.getComment(commentId);

        return new ResponseEntity(result,HttpStatus.OK);
    }

     */
}

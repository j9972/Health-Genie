package com.example.healthgenie.controller;

import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.service.PtReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserPtReviewController {



    private final PtReviewService reviewService;

    @PostMapping("/User/Pt/review")
    public ResponseEntity test(@RequestBody PtReviewRequestDto dto){

        Long UserId = 1L; //임시 -> 회원 기능 후 useradapter에서 받기
//        reviewService.addPtReview(dto,1L);

        PtReviewResponseDto responseDto = reviewService.addPtReview(dto,1L);
        return new ResponseEntity(responseDto,HttpStatus.OK);
    }
}

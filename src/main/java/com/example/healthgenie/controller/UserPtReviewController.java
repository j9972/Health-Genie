package com.example.healthgenie.controller;

import com.example.healthgenie.domain.ptreview.dto.PtReviewDetailResponseDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewListResponseDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.service.PtReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pt")
public class UserPtReviewController {



    private final PtReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity test(@RequestBody PtReviewRequestDto dto){

        Long UserId = 1L; //임시 -> 회원 기능 후 useradapter에서 받기
//        reviewService.addPtReview(dto,1L);

        PtReviewResponseDto responseDto = reviewService.addPtReview(dto,1L);
        return new ResponseEntity(responseDto,HttpStatus.OK);
    }

    @GetMapping("/review/list/trainer")
    public ResponseEntity getReviewListByTrainer(@RequestParam Long trainerId){
        List<PtReviewListResponseDto> result = reviewService.getReviewListByTrainer(trainerId);
        return new ResponseEntity(result,HttpStatus.OK);

    }

    @GetMapping("/review/list/my")
    public ResponseEntity getReviewListByUser(@RequestParam Long userId){
        List<PtReviewListResponseDto> result = reviewService.getReviewListByTrainer(userId);
        return new ResponseEntity(result,HttpStatus.OK);

    }

    @GetMapping("/review/detail")
    public ResponseEntity getReviewDetail(@RequestParam Long reviewId){
        PtReviewDetailResponseDto result = reviewService.getReviewDetail(reviewId);

        return new ResponseEntity(result,HttpStatus.OK);
    }
}

package com.example.healthgenie.boundedContext.ptreview.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.service.PtReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class PtReviewController {

    private final PtReviewService reviewService;

    /*
        user 회원이 작성
        프론트에서 트레이너에게는 후기 작성란이 없기에 작성 불가하게 막아줌.
     */
    @PostMapping("/write")// http://localhost:1234/review/write
    public ResponseEntity<Result> addReview(@RequestBody PtReviewRequestDto dto){

        PtReviewResponseDto response = reviewService.addPtReview(dto);
        return ResponseEntity.ok(Result.of(response));
    }

    // 수정하려고 form 불러오기 -> 조회가 필요함 결국은
    @GetMapping("/{reviewId}") // http://localhost:1234/review
    public ResponseEntity<Result> getReview(@PathVariable Long reviewId){

        PtReviewResponseDto response = reviewService.getPtReview(reviewId);
        return ResponseEntity.ok(Result.of(response));
    }

    /*
        특정 trainer review list 조회 [ 트레이너용 관리페이지 ]
        traienr 랑 user 를 나누는 이유는 review 안에 col 을 보면 userId, trainerId로 나뉘니까 userId 로만 데이터를 가져올 수 없다.
        최근 작성순서로 정렬했기에 프론트에서 상위 3개씩 가져다가 사용하면 된다.
     */
    //
    @GetMapping("/list/trainer/{trainerId}") // http://localhost:1234/review/list/trainer/{trainerId}
    public ResponseEntity<Result> getAllTrainerReview(@PathVariable Long trainerId, @RequestParam(required = false, defaultValue = "0") int page){
        // 5개씩 페이징 처리
        int size = 5;
        Page<PtReviewResponseDto> response = reviewService.getAllTrainerReview(trainerId, page, size);
        return ResponseEntity.ok(Result.of(response));
    }

    /*
        본인이 작성한 review list 조회 [ 회원용 관리페이지 ]
        최근 작성순서로 정렬했기에 프론트에서 상위 3개씩 가져다가 사용하면 된다.
    */
    @GetMapping("/list/my/{userId}") // http://localhost:1234/review/list/{userId}
    public ResponseEntity<Result> getAllMyReview(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "0") int page){
        // 5개씩 페이징 처리
        int size = 5;
        Page<PtReviewResponseDto> response = reviewService.getAllReview(userId, page, size);
        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너 말고 회원 본인만 수정 가능하기
    @PostMapping("/edit/{reviewId}")// http://localhost:1234/review/edit/{reviewId}
    public ResponseEntity<Result> updateReview(@RequestBody PtReviewRequestDto dto, @PathVariable Long reviewId){

        PtReviewResponseDto response = reviewService.updateReview(dto,reviewId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너 말고 회원 본인만 삭제 가능하게 하기
    @DeleteMapping("/member/{reviewId}") // http://localhost:1234/review/member/{reviewId}
    public ResponseEntity<Result> deleteReview(@PathVariable Long reviewId) {
        reviewService.deletePtReview(reviewId);

        return ResponseEntity.ok(Result.of("후기가 삭제 되었습니다."));
    }
}

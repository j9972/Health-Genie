package com.example.healthgenie.boundedContext.ptreview.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewUpdateRequest;
import com.example.healthgenie.boundedContext.ptreview.service.PtReviewService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @PostMapping
    public ResponseEntity<Result> addReview(@RequestBody PtReviewRequestDto dto,
                                            @AuthenticationPrincipal User user){

        PtReviewResponseDto response = reviewService.addPtReview(dto, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 수정하려고 form 불러오기 -> 결국 조회가 필요함
    @GetMapping("/{reviewId}")
    public ResponseEntity<Result> getReview(@PathVariable Long reviewId){

        PtReviewResponseDto response = reviewService.getPtReview(reviewId);
        return ResponseEntity.ok(Result.of(response));
    }

    /*
        특정 trainer review list 조회 [ 트레이너용 관리페이지 ]
        traienr 랑 user 를 나누는 이유는 review 안에 col 을 보면 userId, trainerId로 나뉘니까 userId 로만 데이터를 가져올 수 없다.
        최근 작성순서로 정렬했기에 프론트에서 상위 3개씩 가져다가 사용하면 된다.
     */

    @GetMapping("/trainer/list/{trainerId}")
    public ResponseEntity<Result> getAllTrainerReview(@PathVariable Long trainerId ,@RequestParam(required = false, defaultValue = "0") int page){

        int size = 5; // 5개씩 페이징 처리
        Page<PtReviewResponseDto> response = reviewService.getAllTrainerReview(trainerId, page, size);
        return ResponseEntity.ok(Result.of(response));
    }


    /*
        본인이 작성한 review list 조회 [ 회원용 관리페이지 ]
        최근 작성순서로 정렬했기에 프론트에서 상위 3개씩 가져다가 사용하면 된다.
    */
    @GetMapping("/my/list/{userId}")
    public ResponseEntity<Result> getAllMyReview(@PathVariable Long userId,
                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                 @AuthenticationPrincipal User user){

        log.info("review controller get, own review -> principal user : {}", user);

        int size = 5; // 5개씩 페이징 처리
        Page<PtReviewResponseDto> response = reviewService.getAllReview(userId, page, size, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 후기를 검색으로 찾기
    @GetMapping("/list/findAll")
    public ResponseEntity<Result> findAll(@RequestParam(name = "search", defaultValue = "") String keyword) {
        List<PtReviewResponseDto> response = reviewService.findAll(keyword);

        return ResponseEntity.ok(Result.of(response));
    }

    // 날짜 필터링으로 후기 모아보기
    @GetMapping("/list/dateFilter")
    public ResponseEntity<Result> findAll(@RequestParam(required = false, defaultValue = "1900-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchStartDate,
                                          @RequestParam(required = false, defaultValue = "9999-12-31") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchEndDate) {
        List<PtReviewResponseDto> response = reviewService.findAllByDate(searchStartDate,searchEndDate);

        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너 말고 회원 본인만 수정 가능하기
    @PatchMapping("/{reviewId}")
    public ResponseEntity<Result> updateReview(@RequestBody PtReviewUpdateRequest dto,
                                               @PathVariable Long reviewId,
                                               @AuthenticationPrincipal User user){

        log.info("review controller update -> principal user : {}", user);

        PtReviewResponseDto response = reviewService.updateReview(dto,reviewId, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너 말고 회원 본인만 삭제 가능하게 하기
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Result> deleteReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal User user) {
        log.info("review controller delete -> principal user : {}", user);

        String response = reviewService.deletePtReview(reviewId, user);
        return ResponseEntity.ok(Result.of(response));
    }
}

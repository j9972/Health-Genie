package com.example.healthgenie.boundedContext.ptrecord.controller;


import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessPhotoService;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessService;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessTransactionSerivce;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
@Slf4j
public class PtProcessController {
    private final PtProcessService processService;
    private final PtProcessTransactionSerivce processTransactionSerivce;

    // trainer가 작성
    @PostMapping("/trainer")
    public ResponseEntity<Result> addPtProcess(PtProcessRequestDto dto,
                                               @AuthenticationPrincipal User user)  throws IOException {

        log.info("process controller principal user : {}", user);
        PtProcessResponseDto response = processTransactionSerivce.addPtProcess(dto, user);

        return ResponseEntity.ok(Result.of(response));
    }

    /*
        본인은 본인것만 볼 수 있기에 userId 같은거 필요없다 [ security로 가능 ]

        트레이너가 작성한 전체 피드백 모아보기 [ 트레이너용 관리페이지에서 사용 ]
        관리페이지 : 최근 작성한 글들 순서로 정렬해 놓은 것이기 때문에 상위 3개씩 가져다가 쓰면 된다.
     */
    @GetMapping("/trainer/list")
    public ResponseEntity<Result> getAllTrainerProcess(@RequestParam(required = false, defaultValue = "0") int page,
                                                       @AuthenticationPrincipal User user){

        log.info("process controller -> getAllTrainer principal user : {}", user);
        int size = 5; // 5개씩 페이징 처리
        Page<PtProcessResponseDto> response = processService.getAllTrainerProcess(page, size, user);
        return ResponseEntity.ok(Result.of(response));
    }

    /*
        본인이 관련 모든 피드백 모아보기 [ 회원용 관리페이지에서 사용 ]
        관리페이지 : 최근 작성한 글들 순서로 정렬해 놓은 것이기 때문에 상위 3개씩 가져다가 쓰면 된다.
     */
    @GetMapping("/my/list")
    public ResponseEntity<Result> getAllMyProcess(@RequestParam(required = false, defaultValue = "0") int page,
                                                  @AuthenticationPrincipal User user){

        log.info("process controller -> getAllUser principal user : {}", user);
        int size = 5; // 5개씩 페이징 처리
        Page<PtProcessResponseDto> response = processService.getAllMyProcess(page, size, user);
        return ResponseEntity.ok(Result.of(response));
    }

    // 일지를 검색으로 찾기
    @GetMapping("/list/findAll")
    public ResponseEntity<Result> findAll(@RequestParam(name = "search", defaultValue = "") String keyword) {
        List<PtProcessResponseDto> response = processService.findAll(keyword);

        return ResponseEntity.ok(Result.of(response));
    }

    // 날짜 필터링으로 일지 모아보기
    @GetMapping("/list/dateFilter")
    public ResponseEntity<Result> findAll(@RequestParam(required = false, defaultValue = "1900-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchStartDate,
                                          @RequestParam(required = false, defaultValue = "9999-12-31") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchEndDate) {

        List<PtProcessResponseDto> response = processService.findAllByDate(searchStartDate,searchEndDate);

        return ResponseEntity.ok(Result.of(response));
    }


    @GetMapping("/detail/{processId}")
    public ResponseEntity<Result> getProcess(@PathVariable Long processId){
        PtProcessResponseDto response = processService.getPtProcess(processId);
        return ResponseEntity.ok(Result.of(response));
    }

    // 트레이너만 삭제 기능이 가능
    @DeleteMapping("/{processId}")
    public ResponseEntity<Result> deleteProcess(@PathVariable Long processId,
                                                @AuthenticationPrincipal User user) {

        log.info("process controller -> deleteProcess principal user : {}", user);
        String response = processService.deletePtProcess(processId, user);

        return ResponseEntity.ok(Result.of(response));
    }


}

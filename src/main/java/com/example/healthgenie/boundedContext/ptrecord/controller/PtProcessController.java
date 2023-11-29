package com.example.healthgenie.boundedContext.ptrecord.controller;


import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessPhotoService;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessService;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessTransactionSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @PostMapping("/trainer/write")// http://localhost:1234/process/trainer/write
    public ResponseEntity addPtProcess(PtProcessRequestDto dto)  throws IOException {

        return ResponseEntity.ok(processTransactionSerivce.addPtProcess(dto));
    }

    /*
        트레이너가 작성한 전체 피드백 모아보기 [ 트레이너용 관리페이지에서 사용 ]
        관리페이지 : 최근 작성한 글들 순서로 정렬해 놓은 것이기 때문에 상위 3개씩 가져다가 쓰면 된다.
     */
    @GetMapping("/list/trainer/{trainerId}") // http://localhost:1234/process/list/trainer/{trainerId}
    public Page<PtProcessResponseDto> getAllTrainerProcess(@PathVariable Long trainerId, @RequestParam(required = false, defaultValue = "0") int page){
        // 5개씩 페이징 처리
        int size = 5;
        return processService.getAllTrainerProcess(trainerId, page, size);
    }

    /*
        본인이 관련 모든 피드백 모아보기 [ 회원용 관리페이지에서 사용 ]
        관리페이지 : 최근 작성한 글들 순서로 정렬해 놓은 것이기 때문에 상위 3개씩 가져다가 쓰면 된다.
     */
    @GetMapping("/list/my/{userId}") // http://localhost:1234/process/list/my/{userId}
    public Page<PtProcessResponseDto> getAllMyProcess(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "0") int page){
        // 5개씩 페이징 처리
        int size = 5;
        return processService.getAllMyProcess(userId, page, size);
    }

    @GetMapping("/detail/{processId}") // http://localhost:1234/process/detail/{processId}
    public ResponseEntity getProcess(@PathVariable Long processId){
        PtProcessResponseDto responseDto = processService.getPtProcess(processId);
        return new ResponseEntity(responseDto,HttpStatus.OK);
    }

    // 트레이너만 삭제 기능이 가능
    @DeleteMapping("/trainer/{processId}") // http://localhost:1234/process/trainer/{processId}
    public ResponseEntity deleteProcess(@PathVariable Long processId) {

        processService.deletePtProcess(processId);

        return new ResponseEntity("피드백 삭제가 성공했습니다",HttpStatus.OK);
    }


}

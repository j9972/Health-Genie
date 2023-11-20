package com.example.healthgenie.controller;


import com.example.healthgenie.domain.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.domain.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.domain.ptreview.entity.PtReview;
import com.example.healthgenie.service.PtProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
@Slf4j
public class PtProcessController {
    private final PtProcessService processService;

    @PostMapping("/trainer/write")// http://localhost:1234/process/trainer/write
    public ResponseEntity addPtProcess(@RequestBody PtProcessRequestDto dto){

        Long trainerId = dto.getTrainerId();

        PtProcessResponseDto responseDto = processService.addPtProcess(dto,trainerId);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    // 트레이너가 작성한 전체 피드백 모아보기 -> n+1 문제
    @GetMapping("/list/trainer/{trainerId}") // http://localhost:1234/process/list/trainer/{trainerId}
    public Page<PtProcessResponseDto> getAllTrainerProcess(@PathVariable Long trainerId, @RequestParam(required = false, defaultValue = "0") int page){
        // 5개씩 페이징 처리
        int size = 5;
        return processService.getAllTrainerProcess(trainerId, page, size);
    }

    // 본인이 관련 모든 피드백 모아보기
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
        Long trainerId = processService.findById(processId);
        processService.deletePtProcess(processId, trainerId);

        return new ResponseEntity("피드백 삭제가 성공했습니다",HttpStatus.OK);
    }
}

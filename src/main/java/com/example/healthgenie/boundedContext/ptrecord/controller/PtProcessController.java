package com.example.healthgenie.boundedContext.ptrecord.controller;


import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessPhotoService;
import com.example.healthgenie.boundedContext.ptrecord.service.PtProcessService;
import com.example.healthgenie.base.utils.S3UploadUtils;
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
    private final S3UploadUtils s3UploadUtils;
    private final PtProcessPhotoService ptProcessPhotoService;

    @PostMapping("/trainer/write")// http://localhost:1234/process/trainer/write
    public ResponseEntity addPtProcess(PtProcessRequestDto dto)  throws IOException {

        // 이미지 S3 저장
        List<String> photoPaths = new ArrayList<>();
        if(existsFile(dto)) {
            photoPaths = s3UploadUtils.upload(dto.getPhotos(), "post-photos");
        }

        // PtProcess 엔티티 저장
        PtProcessResponseDto savedProcess = processService.addPtProcess(dto);

        // PtProcessPhoto 엔티티 저장
        if(existsFile(dto)) {
            ptProcessPhotoService.saveAll(savedProcess.getId(), photoPaths);
        }

        PtProcessResponseDto response = PtProcessResponseDto.builder()
                .id(savedProcess.getId())
                .date(savedProcess.getDate())
                .title(savedProcess.getTitle())
                .content(savedProcess.getContent())
                .trainerId(savedProcess.getTrainerId())
                .userId(savedProcess.getUserId())
                .photoPaths(photoPaths)
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
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

        processService.deletePtProcess(processId);

        return new ResponseEntity("피드백 삭제가 성공했습니다",HttpStatus.OK);
    }

    private boolean existsFile(PtProcessRequestDto request) {
        long totalFileSize = request.getPhotos().stream()
                .mapToLong(MultipartFile::getSize)
                .sum();
        return !request.getPhotos().isEmpty() && totalFileSize > 0;
    }
}

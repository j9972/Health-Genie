package com.example.healthgenie.controller;

import com.example.healthgenie.domain.ptrecord.dto.PtProcessDetailResponseDto;
import com.example.healthgenie.domain.ptrecord.dto.PtProcessListResponseDto;
import com.example.healthgenie.domain.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.domain.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import com.example.healthgenie.service.PtProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pt/process")
public class UserPtProcessController {
    private final PtProcessService processService;

    // TODO : 트레이너 입장에서 pt record 수정 및 삭제
    /*
        pt 기록물은 trainer 에 의해서만 작성되지만, 조회는 둘다 가능하게 만들어야 한다
     */
//    @PostMapping("/")// http://localhost:1234/api/v1/pt/process
//    public ResponseEntity addPtProcess(@RequestBody PtProcessRequestDto dto){
//
//        Long trainerId = 1L; // 임시 -> 회원 기능 후 useradapter에서 받기
//
//        PtProcessResponseDto responseDto = processService.addPtProcess(dto,trainerId);
//        return new ResponseEntity(responseDto, HttpStatus.OK);
//    }
//
//    @GetMapping("/list") // http://localhost:1234/api/v1/pt/process/list
//    public ResponseEntity getPtProcessListByTrainer(@RequestParam Long trainerId){
//        List<PtProcessListResponseDto> result = processService.getPtProcessListByTrainer(trainerId);
//        return new ResponseEntity(result,HttpStatus.OK);
//
//    }
//
//    @GetMapping("/detail")       // http://localhost:1234/api/v1/pt/process/detail
//    public ResponseEntity getPtProcessDetail(@RequestParam Long processId){
//        PtProcessDetailResponseDto result = processService.getPtProcessDetail(processId);
//
//        return new ResponseEntity(result,HttpStatus.OK);
//    }
//
//    @PostMapping("/update") // http://localhost:1234/api/v1/pt/process/update
//    public ResponseEntity updateProduct(@RequestBody PtProcessRequestDto dto){
//
//        Long trainerId = 1L; // 임시 -> 회원 기능 후 useradapter에서 받기
//
//        PtProcessResponseDto responseDto = processService.updatePtProcess(dto,trainerId);
//        return new ResponseEntity(responseDto, HttpStatus.OK);
//    }
//
//    @PostMapping("/delete/{processId}") // // http://localhost:1234/api/v1/pt/process/delete/{processId}
//    public ResponseEntity deleteProduct(@PathVariable Long processId) {
//
//        Long trainerId = 1L;
//        PtProcessResponseDto result = processService.deletePtProcess(processId, trainerId);
//
//        return new ResponseEntity(result,HttpStatus.OK);
//    }
}

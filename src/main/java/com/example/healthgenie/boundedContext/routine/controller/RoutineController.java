package com.example.healthgenie.boundedContext.routine.controller;

import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routine")
@Slf4j
public class RoutineController {

     private final RoutineService routineService;

     @PostMapping("/write") // http://localhost:1234/routine/write
     public ResponseEntity writeRoutine(@RequestBody RoutineRequestDto dto) {

         RoutineResponseDto response = routineService.writeRoutine(dto);
         return new ResponseEntity(response, HttpStatus.OK);
     }

     @PostMapping("/update/{routineId}") // http://localhost:1234/routine/update/{routineId}
     public ResponseEntity updateRoutine(@RequestBody RoutineRequestDto dto, @PathVariable Long routineId) {

         RoutineResponseDto response = routineService.updateRoutine(dto,routineId);
         return new ResponseEntity(response,HttpStatus.OK);
     }


    // 전체조회
    @GetMapping("/{userId}") // http://localhost:1234/routine/{userId}
    public List<RoutineResponseDto> getAllRoutines(@PathVariable Long userId) {
         return routineService.getAllMyRoutine(userId);
    }


    // 상세조회
    @GetMapping("/detail/{routineId}") // http://localhost:1234/routine/detail/{routineId}
    public ResponseEntity getRoutine(@PathVariable Long routineId){

        RoutineResponseDto response = routineService.getMyRoutine(routineId);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    // 본인만 삭제 가능하게 하기 -> 프론트에서 기능을 숨기면 되어서 구별 로직뺌
    @DeleteMapping("/delete/{routineId}") // http://localhost:1234/routine/delete/{routineId}
    public ResponseEntity deleteRoutine(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);

        return new ResponseEntity("후기 삭제를 성공했습니다",HttpStatus.OK);
    }


}

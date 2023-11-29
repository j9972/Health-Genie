package com.example.healthgenie.boundedContext.routine.controller;

import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
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


    // 개인 루틴 전체조회
    @GetMapping("/{userId}") // http://localhost:1234/routine/{userId}
    public List<RoutineResponseDto> getAllRoutines(@PathVariable Long userId) {
         return routineService.getAllMyRoutine(userId);
    }

    /*
        개인 루틴 상세조회
        회원용/트레이너용 관리페이지에서 조회할때 사용할 API
     */
    @GetMapping("/detail/{day}/{userId}") // http://localhost:1234/routine/detail/{day}/{userId}
    public List<RoutineResponseDto> getRoutine(@PathVariable Day day, @PathVariable Long userId){
        return routineService.getMyRoutine(day,userId);
    }

    // 지니 - 초/중/고 급자 전체 조회
    @GetMapping("/genie/{level}/{userId}") // http://localhost:1234/routine/genie/{level}/{userId}
    public List<RoutineResponseDto> getAllGenieRoutines(@PathVariable Level level, @PathVariable Long userId) {
        return routineService.getAllGenieRoutine(level,userId);
    }

    /*
        지니 - 초/중/고 급자 상세 조회 -> 요일에 대한 상세 조회인데 list 조회
        회원용/트레이너용 관리페이지에서 조회할때 사용할 API
     */
    @GetMapping("/genie/detail/{level}/{day}") // http://localhost:1234/routine/genie/detail/{level}/{day}
    public ResponseEntity getGenieRoutine(@PathVariable Level level, @PathVariable Day day) {

        List<RoutineResponseDto> response = routineService.getGenieRoutine(level, day);
        return new ResponseEntity(response,HttpStatus.OK);
    }



    // 본인만 삭제 가능하게 하기 -> 프론트에서 기능을 숨기면 되어서 구별 로직뺌
    @DeleteMapping("/delete/{routineId}") // http://localhost:1234/routine/delete/{routineId}
    public ResponseEntity deleteRoutine(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);

        return new ResponseEntity("후기 삭제를 성공했습니다",HttpStatus.OK);
    }


}

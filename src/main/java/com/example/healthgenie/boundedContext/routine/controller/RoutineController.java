package com.example.healthgenie.boundedContext.routine.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewQueryRepository;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.repository.RoutineQueryRepository;
import com.example.healthgenie.boundedContext.routine.service.RoutineService;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/routine")
public class RoutineController {

     private final RoutineService routineService;

     @PostMapping("/write") // http://localhost:1234/routine/write
     public ResponseEntity<Result> writeRoutine(@RequestBody RoutineRequestDto dto, @AuthenticationPrincipal User user) {

         RoutineResponseDto response = routineService.writeRoutine(dto, user);
         return ResponseEntity.ok(Result.of(response));
     }

     @PostMapping("/update/{routineId}") // http://localhost:1234/routine/update/{routineId}
     public ResponseEntity<Result> updateRoutine(@RequestBody RoutineRequestDto dto,
                                                 @PathVariable Long routineId,
                                                 @AuthenticationPrincipal User user) {

         RoutineResponseDto response = routineService.updateRoutine(dto,routineId, user);
         return ResponseEntity.ok(Result.of(response));
     }


    // 개인 루틴 전체조회 -> currentUser 사용하면 userId 필요없다
    @GetMapping() // http://localhost:1234/routine
    public ResponseEntity<Result> getAllRoutines(@AuthenticationPrincipal User user) {
        List<RoutineResponseDto> response = routineService.getAllMyRoutine(user.getId());
        return ResponseEntity.ok(Result.of(response));
    }

    /*
        개인 루틴 상세조회
        회원용/트레이너용 관리페이지에서 조회할때 사용할 API
     */
    @GetMapping("/detail/{day}") // http://localhost:1234/routine/detail/{day}
    public ResponseEntity<Result> getRoutine(@PathVariable Day day, @AuthenticationPrincipal User user){
        List<RoutineResponseDto> response = routineService.getMyRoutine(day, user.getId());
        return ResponseEntity.ok(Result.of(response));
    }

    // 지니 - 초/중/고 급자 전체 조회
    @GetMapping("/genie/{level}") // http://localhost:1234/routine/genie/{level}
    public ResponseEntity<Result> getAllGenieRoutines(@PathVariable Level level, @AuthenticationPrincipal User user) {
        List<RoutineResponseDto> response = routineService.getAllGenieRoutine(level, user);
        return ResponseEntity.ok(Result.of(response));
    }

    /*
        지니 - 초/중/고 급자 상세 조회 -> 요일에 대한 상세 조회인데 list 조회
        회원용/트레이너용 관리페이지에서 조회할때 사용할 API
     */
    @GetMapping("/genie/detail/{level}/{day}") // http://localhost:1234/routine/genie/detail/{level}/{day}
    public ResponseEntity<Result> getGenieRoutine(@PathVariable Level level, @PathVariable Day day) {

        List<RoutineResponseDto> response = routineService.getGenieRoutine(level, day);
        return ResponseEntity.ok(Result.of(response));
    }



    // 본인만 삭제 가능하게 하기 -> 프론트에서 기능을 숨기면 되어서 구별 로직뺌
    @DeleteMapping("/delete/{routineId}") // http://localhost:1234/routine/delete/{routineId}
    public ResponseEntity<Result> deleteRoutine(@PathVariable Long routineId,
                                                @AuthenticationPrincipal User user) {
        String response = routineService.deleteRoutine(routineId, user);

        return ResponseEntity.ok(Result.of(response));
    }


}

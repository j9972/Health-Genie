package com.example.healthgenie.boundedContext.routine.service;

import com.example.healthgenie.base.exception.RoutineErrorResult;
import com.example.healthgenie.base.exception.RoutineException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.*;
import com.example.healthgenie.boundedContext.routine.repository.RoutineQueryRepository;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    private final RoutineQueryRepository routineQueryRepository;

    // own routine 관련 해서 level 언급은 필요 없다
    @Transactional
    public RoutineResponseDto writeRoutine(RoutineRequestDto dto) {

        Routine saved = new Routine();
        User currentUser = SecurityUtils.getCurrentUser();

        boolean valid = dto.getWriter().equals(currentUser.getNickname());
        validNickname(valid, RoutineErrorResult.DIFFERNET_NICKNAME);

        for (WorkoutRecipe recipe : dto.getWorkoutRecipes()) {

            WorkoutRecipe data = new WorkoutRecipe(recipe.getName(), recipe.getKg(), recipe.getSets(), recipe.getReps());

            Routine routine = Routine.builder()
                    .day(dto.getDay())
                    .parts(dto.getParts())
                    .workoutRecipe(data)
                    .member(currentUser)
                    .build();

            saved = routineRepository.save(routine);
        }

        return RoutineResponseDto.ofOwn(saved);

    }

    @Transactional
    public RoutineResponseDto updateRoutine(RoutineRequestDto dto, Long routineId) {
        Routine routine = authorizationWriter(routineId);

        WorkoutRecipe workoutRecipe = routine.getWorkoutRecipe();

        if(dto.getDay() != null) {
            routine.updateDay(dto.getDay());
        }
        if(dto.getParts() != null) {
            routine.updatePart(dto.getParts());
        }

        for (WorkoutRecipe recipe : dto.getWorkoutRecipes()) {
            if(recipe.getName() != null) {
                workoutRecipe.updateName(recipe.getName());
            }
            // 0도 유효한 값으로 처리
            if(recipe.getSets() != 0 || workoutRecipe.getSets() == 0) {
                workoutRecipe.updateSets(recipe.getSets());
            }
            // 0도 유효한 값으로 처리
            if(recipe.getReps() != 0 || workoutRecipe.getReps() == 0) {
                workoutRecipe.updateReps(recipe.getReps());
            }
            // 0도 유효한 값으로 처리
            if(recipe.getKg() != 0 || workoutRecipe.getKg() == 0) {
                workoutRecipe.updateKg(recipe.getKg());
            }
        }

        return RoutineResponseDto.ofOwn(routine);
    }

    private static void validNickname(boolean routine, RoutineErrorResult validError) {
        if (!routine) {
            throw new RoutineException(validError);
        }
    }


    // 나의 루틴 요일 상관없이 전체 조회 [ userId 로만 구분 ]
    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getAllMyRoutine() {

        Long userId = SecurityUtils.getCurrentUserId();

        return RoutineResponseDto.ofOwn(routineQueryRepository.findAllByMemberId(userId));
    }

    // 나의 루틴 요일별 상세조회
    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getMyRoutine(Day day) {

        Long userId = SecurityUtils.getCurrentUserId();
        return RoutineResponseDto.ofOwn(routineQueryRepository.findAllByMemberIdAndDay(userId, day));
    }

    /*
        로그인된 유저만 사용 가능
        지니 레벨을 선택할 때 마다 level 변동 시키기
     */
    @Transactional
    public List<RoutineResponseDto> getAllGenieRoutine(Level level) {
        User currentUser = SecurityUtils.getCurrentUser();
        currentUser.updateLevel(level); // 이 부분 [ 매번 level update ]
        return RoutineResponseDto.ofGenie(routineQueryRepository.findAllByLevel(level));
    }


    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getGenieRoutine(Level level, Day day) {
        return RoutineResponseDto.ofGenie(routineQueryRepository.findAllByLevelAndDay(level,day));
    }



    @Transactional
    public String deleteRoutine(Long routineId) {
        Routine own = authorizationWriter(routineId);
        routineRepository.deleteById(own.getId());

        return "루틴이 삭제되었습니다.";
    }

    public Routine authorizationWriter(Long id) {
        User member = SecurityUtils.getCurrentUser();

        Routine routine = routineRepository.findById(id).orElseThrow(() -> new RoutineException(RoutineErrorResult.NO_HISTORY));

        if (!routine.getMember().getId().equals(member.getId())) {
            throw new RoutineException(RoutineErrorResult.NO_USER_INFO);
        }
        return routine;

    }

}

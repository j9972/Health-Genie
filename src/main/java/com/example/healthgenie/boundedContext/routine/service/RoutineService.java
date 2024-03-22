package com.example.healthgenie.boundedContext.routine.service;


import com.example.healthgenie.base.exception.Routine.RoutineException;
import com.example.healthgenie.boundedContext.routine.dto.RoutineDeleteResponseDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineUpdateRequestDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineQueryRepository;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    private final RoutineQueryRepository routineQueryRepository;

    @Transactional
    public RoutineResponseDto writeRoutine(RoutineRequestDto dto, User user) {

        Routine saved = new Routine();

        boolean valid = dto.getWriter().equals(user.getNickname());
        validNickname(valid);

        List<Routine> routines = routineRepository.findAllByMemberId(user.getId());

        if (!routines.isEmpty()) {
            for (Routine mine : routines) {
                if (mine.getDay().equals(dto.getDay())) {
                    log.warn("중복된 요일입니다.");
                    throw RoutineException.DUPLICATE_DAY;
                }
            }
        }

        for (WorkoutRecipe recipe : dto.getWorkoutRecipe()) {

            WorkoutRecipe data = new WorkoutRecipe(recipe.getName(), recipe.getKg(), recipe.getSets(),
                    recipe.getReps());

            Routine routine = dto.toEntity(user, data);

            saved = routineRepository.save(routine);
        }

        return RoutineResponseDto.ofOwn(saved);
    }

    @Transactional
    public RoutineResponseDto updateRoutine(RoutineUpdateRequestDto dto, Long routineId, User user) {
        Routine routine = authorizationWriter(routineId, user);
        updateEachRoutineItems(dto, routine);

        return RoutineResponseDto.ofOwn(routine);
    }

    private void updateEachRoutineItems(RoutineUpdateRequestDto dto, Routine routine) {
        WorkoutRecipe workoutRecipe = routine.getWorkoutRecipe();

        if (dto.hasDay()) {
            routine.updateDay(dto.getDay());
        }
        if (dto.hasParts()) {
            routine.updatePart(dto.getParts());
        }

        for (WorkoutRecipe recipe : dto.getWorkoutRecipe()) {

            if (recipe.getName() != null) {
                workoutRecipe.updateName(recipe.getName());
            }
            // 0도 유효한 값으로 처리
            if (recipe.getSets() != 0 || workoutRecipe.getSets() == 0) {
                workoutRecipe.updateSets(recipe.getSets());
            }
            // 0도 유효한 값으로 처리
            if (recipe.getReps() != 0 || workoutRecipe.getReps() == 0) {
                workoutRecipe.updateReps(recipe.getReps());
            }
            // 0도 유효한 값으로 처리
            if (recipe.getKg() != 0 || workoutRecipe.getKg() == 0) {
                workoutRecipe.updateKg(recipe.getKg());
            }
        }
    }


    // 나의 루틴 요일 상관없이 전체 조회 [ userId 로만 구분 ]
    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getAllMyRoutine(Long userId) {
        return RoutineResponseDto.ofOwn(routineQueryRepository.findAllByMemberId(userId));
    }

    // 나의 루틴 요일별 상세조회
    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getMyRoutine(Day day, Long userId) {
        return RoutineResponseDto.ofOwn(routineQueryRepository.findAllByMemberIdAndDay(userId, day));
    }

    /*
        로그인된 유저만 사용 가능
        지니 레벨을 선택할 때 마다 level 변동 시키기
     */
    @Transactional
    public List<RoutineResponseDto> getAllGenieRoutine(Level level, User user) {
        user.updateLevel(level); // 이 부분 [ 매번 level update ]
        return RoutineResponseDto.ofGenie(routineQueryRepository.findAllByLevel(level));
    }


    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getGenieRoutine(Level level, Day day) {
        return RoutineResponseDto.ofGenie(routineQueryRepository.findAllByLevelAndDay(level, day));
    }


    @Transactional
    public RoutineDeleteResponseDto deleteRoutine(Long routineId, User user) {
        Routine own = authorizationWriter(routineId, user);
        routineRepository.deleteById(own.getId());

        return RoutineDeleteResponseDto.builder()
                .id(own.getId())
                .build();
    }

    private Routine authorizationWriter(Long id, User member) {

        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> RoutineException.NO_HISTORY);

        if (!routine.getMember().getId().equals(member.getId())) {
            log.warn("member doesn't have authentication , routine.getMember {}", routine.getMember());
            throw RoutineException.NO_USER_INFO;
        }
        return routine;

    }

    private void validNickname(boolean routine) {
        if (!routine) {
            log.warn("routine valid : false");
            throw RoutineException.DIFFERENT_NICKNAME;
        }
    }

}

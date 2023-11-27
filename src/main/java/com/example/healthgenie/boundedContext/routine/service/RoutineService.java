package com.example.healthgenie.boundedContext.routine.service;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.RoutineErrorResult;
import com.example.healthgenie.base.exception.RoutineException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.*;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.example.healthgenie.boundedContext.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.healthgenie.base.exception.CommonErrorResult.ALREADY_EXISTS_ROLE;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;

    // own routine 관련 해서 level 언급은 필요 없다
    @Transactional
    public RoutineResponseDto writeRoutine(RoutineRequestDto dto) {
        User currentUser = SecurityUtils.getCurrentUser();

        WorkoutRecipe recipe = new WorkoutRecipe(dto.getParts(), dto.getWorkoutName(),dto.getSets(), dto.getReps());

        Routine routine = Routine.builder()
                .day(dto.getDay())
                .content(dto.getContent())
                .workoutRecipe(recipe)
                .member(currentUser)
                .build();


        Routine saved = routineRepository.save(routine);

        return RoutineResponseDto.ofOwn(saved);
    }

    @Transactional
    public RoutineResponseDto updateRoutine(RoutineRequestDto dto, Long routineId) {
        Routine routine = authorizationWriter(routineId);
        return null;
    }
/*
        Optional<Integer> reps = Optional.of(dto.getReps());
        Optional<Integer> sets = Optional.of(dto.getSets());

        if(dto.getWorkoutName() != null) {
            routine.updateWorkoutName(dto.getWorkoutName());
        }
        if(reps != null) {
            routine.updateReps(dto.getReps());
        }
        if(dto.getDay() != null) {
            routine.updateDay(dto.getDay());
        }
        if(sets != null) {
            routine.updateSets(dto.getSets());
        }
        if(dto.getParts() != null) {
            routine.updateParts(dto.getParts());
        }
        if(dto.getContent() != null) {
            routine.updateContent(dto.getContent());
        }

        return RoutineResponseDto.ofOwn(routine);
    }

 */

    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getAllMyRoutine(Long userId) {

        List<Routine> own = routineRepository.findAllByMemberId(userId);
        return own.stream()
                .map(RoutineResponseDto::ofOwn)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public RoutineResponseDto getMyRoutine(Long routineId) {
        Routine own = routineRepository.findById(routineId).orElseThrow(
                () -> new RoutineException(RoutineErrorResult.NO_HISTORY));

        return RoutineResponseDto.ofOwn(own);
    }

    /*
        지니 레벨을 선택할 때 마다 level 변동 시키기
     */


    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getAllGenieRoutine(Long userId, Level level) {
        User user = userServiceImpl.findById(userId);
        user.updateLevel(level); // 이 부분 [ 매번 level update ]

        List<Routine> genie = routineRepository.findByLevel(level);
        return genie.stream()
                .map(RoutineResponseDto::ofGenie)
                .collect(toList());
    }


    @Transactional(readOnly = true)
    public List<RoutineResponseDto> getGenieRoutine(Level level, Day day) {
        List<Routine> genie = routineRepository.findByLevelAndDay(level,day);

        return genie.stream()
                .map(RoutineResponseDto::ofGenie)
                .collect(toList());
    }



    @Transactional
    public void deleteRoutine(Long routineId) {
        Routine own = authorizationWriter(routineId);
        routineRepository.deleteById(own.getId());
    }



    public User isMemberCurrent() {
        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() ->  new RoutineException(RoutineErrorResult.NO_USER_INFO));
    }

    public Routine authorizationWriter(Long id) {
        User member = isMemberCurrent();

        Routine routine = routineRepository.findById(id).orElseThrow(() -> new RoutineException(RoutineErrorResult.NO_HISTORY));
        if (!routine.getMember().equals(member)) {
            throw new RoutineException(RoutineErrorResult.NO_USER_INFO);
        }
        return routine;
    }

}

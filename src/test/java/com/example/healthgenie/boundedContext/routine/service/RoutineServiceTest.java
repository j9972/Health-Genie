package com.example.healthgenie.boundedContext.routine.service;

import static com.example.healthgenie.base.exception.ErrorCode.DUPLICATED;
import static com.example.healthgenie.base.exception.ErrorCode.UNKNOWN_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.routine.dto.RoutineDeleteResponseDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineUpdateRequestDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.util.TestKrUtils;
import com.example.healthgenie.util.TestSyUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoutineServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    TestKrUtils testKrUtils;

    @Autowired
    RoutineService routineService;

    User user;
    User user2;
    Routine routine;
    Routine beginnerGenie;
    Routine intermediateGenie;
    Routine expertGenie;
    Routine failRoutine;


    @BeforeEach
    void before() {
        List<WorkoutRecipe> workoutRecipeList = new ArrayList<>();
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3, 3, 3);
        WorkoutRecipe recipe2 = new WorkoutRecipe("페이스 풀", 3, 3, 3);
        workoutRecipeList.add(recipe);
        workoutRecipeList.add(recipe2);

        user = testKrUtils.createUser("jh485200@gmail.com", "test1", AuthProvider.EMPTY, Role.USER);
        user2 = testKrUtils.createUser("test@gmail.com", "test2", AuthProvider.EMPTY, Role.USER);
        routine = testSyUtils.writeRoutine(Day.WEDNESDAY, "하체,가슴", workoutRecipeList, user);

        beginnerGenie = testSyUtils.genieRoutine(Level.BEGINNER, Day.FRIDAY, "test", "하체,가슴", recipe);
        intermediateGenie = testSyUtils.genieRoutine(Level.INTERMEDIATE, Day.FRIDAY, "test", "하체,가슴", recipe);
        expertGenie = testSyUtils.genieRoutine(Level.EXPERT, Day.FRIDAY, "test", "하체,가슴", recipe);

        failRoutine = testSyUtils.genieRoutine(Level.EMPTY, Day.FRIDAY, "test", "하체,가슴", recipe);
    }

    /***
     * 레드 그린 사이클 만들자
     */

    @Test
    @DisplayName("루틴 작성하기")
    void write_routine() {
        // given

        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3, 3, 3);

        RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.FRIDAY
                , "하체, 어깨", Collections.singletonList(recipe), user.getNickname());

        // when
        RoutineResponseDto savedRoutine = routineService.writeRoutine(dto, user);

        WorkoutRecipe testRecipe = savedRoutine.getRecipe();

        String name = testRecipe.getName();
        int kg = testRecipe.getKg();
        int sets = testRecipe.getSets();
        int reps = testRecipe.getReps();

        // then
        assertNotNull(savedRoutine);

        assertThat(savedRoutine.getParts()).isEqualTo("하체, 어깨");
        assertThat(savedRoutine.getDay()).isEqualTo(Day.FRIDAY);

        assertThat(name).isEqualTo("스쿼트");
        assertThat(kg).isEqualTo(3);
        assertThat(sets).isEqualTo(3);
        assertThat(reps).isEqualTo(3);
    }

    @Test
    @DisplayName("중복된 요일 루틴 작성 실패")
    void fail_add_routine_cuz_of_duplicated_day() {
        // given

        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3, 3, 3);

        // when

        // then
        assertThatThrownBy(() -> {
            for (int i = 0; i < 5; i++) {
                RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.MONDAY
                        , "하체, 어깨", Collections.singletonList(recipe), user.getNickname());
                routineService.writeRoutine(dto, user);
                throw new CustomException(DUPLICATED);
            }

        }).isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("루틴 수정하기")
    void update_routine() {
        // given
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3, 3, 3);

        RoutineUpdateRequestDto dto = testSyUtils.createOwnRoutineUpdateRequest(Day.FRIDAY
                , "하체, 어깨", Collections.singletonList(recipe));

        // when
        RoutineResponseDto savedRoutine = routineService.updateRoutine(dto, routine.getId(), user);

        // then
        WorkoutRecipe testRecipe = savedRoutine.getRecipe();

        String name = testRecipe.getName();
        int kg = testRecipe.getKg();
        int sets = testRecipe.getSets();
        int reps = testRecipe.getReps();

        assertNotNull(savedRoutine);

        assertThat(savedRoutine.getParts()).isEqualTo("하체, 어깨");
        assertThat(savedRoutine.getDay()).isEqualTo(Day.FRIDAY);

        assertThat(name).isEqualTo("스쿼트");
        assertThat(kg).isEqualTo(3);
        assertThat(sets).isEqualTo(3);
        assertThat(reps).isEqualTo(3);
    }


    @Test
    @DisplayName("나의 루틴 모두 조회하기")
    void get_all_my_routine() {
        // given
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3, 3, 3);

        for (int i = 0; i < 1; i++) {
            RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.MONDAY
                    , "하체, 어깨", Collections.singletonList(recipe), user.getNickname());
            routineService.writeRoutine(dto, user);
        }

        // when
        List<RoutineResponseDto> response = routineService.getAllMyRoutine(user.getId());

        // then
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("다른 사람 루틴 모두 조회 - 실패")
    void fail_get_all_other_routine() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (!routine.getMember().equals(user2)) {
                throw new CustomException(UNKNOWN_EXCEPTION);
            }
        }).isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("나의 루틴 요일별 상세조회")
    void get_my_routine() {
        // given

        // when
        List<RoutineResponseDto> wedRoutine = routineService.getMyRoutine(Day.WEDNESDAY, user.getId());

        // then
        assertThat(wedRoutine.size()).isEqualTo(2);
        assertThat(wedRoutine.get(0).getDay()).isEqualTo(Day.WEDNESDAY);
        assertThat(wedRoutine.get(0).getContent()).isEqualTo(routine.getContent());
        assertThat(wedRoutine.get(0).getParts()).isEqualTo(routine.getParts());
        assertThat(wedRoutine.get(0).getRecipe()).isEqualTo(routine.getWorkoutRecipe());
    }

    @Test
    @DisplayName("level 별 지니 루틴 모두 조회하기")
    void get_all_genie_routine() {
        // given

        // when
        List<RoutineResponseDto> beginG = routineService.getAllGenieRoutine(Level.BEGINNER, user);
        List<RoutineResponseDto> InterG = routineService.getAllGenieRoutine(Level.INTERMEDIATE, user);
        List<RoutineResponseDto> expertG = routineService.getAllGenieRoutine(Level.EXPERT, user);

        // then
        assertThat(beginnerGenie.getParts()).isEqualTo(beginG.get(0).getParts());
        assertThat(beginnerGenie.getWorkoutRecipe()).isEqualTo(beginG.get(0).getRecipe());

        assertThat(intermediateGenie.getParts()).isEqualTo(InterG.get(0).getParts());
        assertThat(intermediateGenie.getWorkoutRecipe()).isEqualTo(InterG.get(0).getRecipe());

        assertThat(expertGenie.getParts()).isEqualTo(expertG.get(0).getParts());
        assertThat(expertGenie.getWorkoutRecipe()).isEqualTo(expertG.get(0).getRecipe());

    }


    @Test
    @DisplayName("level이 empty면 지니 루틴 조회 실패")
    void fail_genie_routine_cuz_of_empty_level() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            if (failRoutine.getLevel().equals(Level.EMPTY)) {
                throw new CustomException(UNKNOWN_EXCEPTION);
            }
        }).isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("해당 요일의 지니 루틴 조회하기")
    void get_genie_routine() {
        // given
        List<RoutineResponseDto> begin = routineService.getGenieRoutine(Level.BEGINNER, Day.FRIDAY);
        List<RoutineResponseDto> inter = routineService.getGenieRoutine(Level.BEGINNER, Day.FRIDAY);
        List<RoutineResponseDto> expert = routineService.getGenieRoutine(Level.BEGINNER, Day.FRIDAY);

        // when

        // then=
        assertThat(beginnerGenie.getParts()).isEqualTo(begin.get(0).getParts());
        assertThat(beginnerGenie.getWorkoutRecipe()).isEqualTo(begin.get(0).getRecipe());

        assertThat(intermediateGenie.getParts()).isEqualTo(begin.get(0).getParts());
        assertThat(intermediateGenie.getWorkoutRecipe()).isEqualTo(inter.get(0).getRecipe());

        assertThat(expertGenie.getParts()).isEqualTo(begin.get(0).getParts());
        assertThat(expertGenie.getWorkoutRecipe()).isEqualTo(expert.get(0).getRecipe());
    }

    @Test
    @DisplayName("루틴 삭제하기")
    void delete_routine() {
        // given

        // when
        RoutineDeleteResponseDto response = routineService.deleteRoutine(routine.getId(), user);

        // then
        assertThat(response.getId()).isEqualTo(routine.getId());
    }

    @Test
    @DisplayName("존재 하지 않은 루틴 삭제하기")
    void fail_routine_delete_cuz_of_no_routine_history() {
        // given
        // when

        // then
        assertThatThrownBy(() -> routineService.deleteRoutine(2000L, user))
                .isInstanceOf(CustomException.class);
    }

}
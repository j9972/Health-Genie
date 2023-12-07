package com.example.healthgenie.boundedContext.routine.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestSyUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoutineServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    RoutineService routineService;

    User user;
    Routine routine;
    Routine beginnerGenie;
    Routine intermediateGenie;
    Routine expertGenie;
    Routine failRoutine;


    @BeforeEach
    void before() {
        List<String> parts = Arrays.asList("하체","가슴");
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3,3,3);

        user = testSyUtils.createUser("test1",Role.USER,"jh485200@gmail.com");
        routine = testSyUtils.writeRoutine(Day.WEDNESDAY,"test",parts,recipe,user);

        beginnerGenie = testSyUtils.genieRoutine(Level.BEGINNER, Day.FRIDAY, "test", parts, recipe);
        intermediateGenie = testSyUtils.genieRoutine(Level.INTERMEDIATE, Day.FRIDAY, "test", parts, recipe);
        expertGenie = testSyUtils.genieRoutine(Level.EXPERT, Day.FRIDAY, "test", parts, recipe);

        failRoutine = testSyUtils.genieRoutine(Level.EMPTY, Day.FRIDAY, "test", parts, recipe);
    }

    /***
     * 레드 그린 사이클 만들자
     */

    @Test
    @DisplayName("루틴 작성하기")
    void writeRoutine() {
        // given
        testSyUtils.login(user);

        List<String> parts = Arrays.asList("하체","어꺠");
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3,3,3);

        RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.FRIDAY
                ,"test content", parts, recipe , user.getNickname());

        // when
        RoutineResponseDto savedRoutine = routineService.writeRoutine(dto);

        WorkoutRecipe testRecipe = savedRoutine.getRecipe();

        String name = testRecipe.getName();
        int kg = testRecipe.getKg();
        int sets = testRecipe.getSets();
        int reps = testRecipe.getReps();

        // then
        assertNotNull(savedRoutine);

        assertIterableEquals(savedRoutine.getParts(), parts);
        assertThat(savedRoutine.getDay()).isEqualTo(Day.FRIDAY);
        assertThat(savedRoutine.getContent()).isEqualTo("test content");

        assertThat(name).isEqualTo("스쿼트");
        assertThat(kg).isEqualTo(3);
        assertThat(sets).isEqualTo(3);
        assertThat(reps).isEqualTo(3);
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 루틴 작성하기")
    void notLoginWriteRoutine() {
        // given

        List<String> parts = Arrays.asList("하체","어꺠");
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3,3,3);

        RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.FRIDAY
                ,"test content", parts, recipe , "user");

        // when

        // then
        assertThatThrownBy(() -> routineService.writeRoutine(dto))
                .isInstanceOf(CommonException.class);
    }


    @Test
    @DisplayName("루틴 수정하기")
    void updateRoutine() {

        /*
            본인 루틴은 본인만 보기에 타인이 수정하거나 삭제할 일이 없다.
         */

        // given
        testSyUtils.login(user);


        // when
        List<String> parts = Arrays.asList("하체","어꺠");
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3,3,3);

        RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.FRIDAY
                ,"test content", parts, recipe , user.getNickname());


        // then
        RoutineResponseDto savedRoutine = routineService.updateRoutine(dto, routine.getId());

        WorkoutRecipe testRecipe = savedRoutine.getRecipe();

        String name = testRecipe.getName();
        int kg = testRecipe.getKg();
        int sets = testRecipe.getSets();
        int reps = testRecipe.getReps();


        assertNotNull(savedRoutine);

        assertIterableEquals(savedRoutine.getParts(), parts);
        assertThat(savedRoutine.getDay()).isEqualTo(Day.FRIDAY);
        assertThat(savedRoutine.getContent()).isEqualTo("test content");

        assertThat(name).isEqualTo("스쿼트");
        assertThat(kg).isEqualTo(3);
        assertThat(sets).isEqualTo(3);
        assertThat(reps).isEqualTo(3);
    }


    @Test
    @DisplayName("나의 루틴 모두 조회하기")
    void getAllMyRoutine() {
        // given
        testSyUtils.login(user);

        List<String> parts = Arrays.asList("하체","어꺠");
        WorkoutRecipe recipe = new WorkoutRecipe("스쿼트", 3,3,3);

        for(int i = 0; i < 5; i++) {
            RoutineRequestDto dto = testSyUtils.createOwnRoutineRequest(Day.FRIDAY
                    , "test content", parts, recipe, user.getNickname());
            routineService.writeRoutine(dto);
        }

        // when
        List<RoutineResponseDto> response = routineService.getAllMyRoutine();

        // then
        /*
            @before에 만들어 놓은 것까지 6개다
         */
        assertThat(response.size()).isEqualTo(6);
    }


    @Test
    @DisplayName("나의 루틴 요일별 상세조회")
    void getMyRoutine() {
        // given
        testSyUtils.login(user);

        // when
        // before에 있는 값을 가져오기
        List<RoutineResponseDto> wedRoutine = routineService.getMyRoutine(Day.WEDNESDAY);

        // then
        assertThat(wedRoutine.size()).isEqualTo(1);
        assertThat(wedRoutine.get(0).getWriter()).isEqualTo(routine.getMember().getNickname());
        assertThat(wedRoutine.get(0).getDay()).isEqualTo(Day.WEDNESDAY);
        assertThat(wedRoutine.get(0).getContent()).isEqualTo(routine.getContent());
        assertThat(wedRoutine.get(0).getParts()).isEqualTo(routine.getPart());
        assertThat(wedRoutine.get(0).getRecipe()).isEqualTo(routine.getWorkoutRecipe());
    }

    @Test
    @DisplayName("level 별 지니 루틴 모두 조회하기")
    void getAllGenieRoutine() {
        // given
        testSyUtils.login(user);

        // when
        List<RoutineResponseDto> beginG = routineService.getAllGenieRoutine(Level.BEGINNER);
        List<RoutineResponseDto> InterG = routineService.getAllGenieRoutine(Level.INTERMEDIATE);
        List<RoutineResponseDto> expertG = routineService.getAllGenieRoutine(Level.EXPERT);


        // then
        assertThat(beginnerGenie.getLevel()).isEqualTo(beginG.get(0).getLevel());
        assertThat(beginnerGenie.getPart()).isEqualTo(beginG.get(0).getParts());
        assertThat(beginnerGenie.getWorkoutRecipe()).isEqualTo(beginG.get(0).getRecipe());

        assertThat(intermediateGenie.getLevel()).isEqualTo(InterG.get(0).getLevel());
        assertThat(intermediateGenie.getPart()).isEqualTo(InterG.get(0).getParts());
        assertThat(intermediateGenie.getWorkoutRecipe()).isEqualTo(InterG.get(0).getRecipe());

        assertThat(expertGenie.getLevel()).isEqualTo(expertG.get(0).getLevel());
        assertThat(expertGenie.getPart()).isEqualTo(expertG.get(0).getParts());
        assertThat(expertGenie.getWorkoutRecipe()).isEqualTo(expertG.get(0).getRecipe());

    }


    @Test
    @DisplayName("level이 empty면 지니 루틴 조회 실패")
    void failGenieRoutine() {
        // given
        testSyUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> {
            if (failRoutine.getLevel().equals(Level.EMPTY)) {
                throw new CommonException(CommonErrorResult.BAD_REQUEST);
            }
        }).isInstanceOf(CommonException.class);
    }


    @Test
    @DisplayName("해당 요일의 지니 루틴 조회하기")
    void getGenieRoutine() {
        // given
        List<RoutineResponseDto> begin = routineService.getGenieRoutine(Level.BEGINNER, Day.FRIDAY);
        List<RoutineResponseDto> inter = routineService.getGenieRoutine(Level.BEGINNER, Day.FRIDAY);
        List<RoutineResponseDto> expert = routineService.getGenieRoutine(Level.BEGINNER, Day.FRIDAY);


        // when

        // then
        assertIterableEquals(beginnerGenie.getPart(), begin.get(0).getParts());
        assertThat(beginnerGenie.getWorkoutRecipe()).isEqualTo(begin.get(0).getRecipe());

        assertIterableEquals(intermediateGenie.getPart(), inter.get(0).getParts());
        assertThat(intermediateGenie.getWorkoutRecipe()).isEqualTo(inter.get(0).getRecipe());

        assertIterableEquals(expertGenie.getPart(), expert.get(0).getParts());
        assertThat(expertGenie.getWorkoutRecipe()).isEqualTo(expert.get(0).getRecipe());
    }

    @Test
    @DisplayName("루틴 삭제하기")
    void deleteRoutine() {
        // given
        testSyUtils.login(user);

        // when

        // then
        String response = routineService.deleteRoutine(routine.getId());
        assertThat(response).isEqualTo("루틴이 삭제되었습니다.");
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 루틴 삭제하기")
    void notLogindeleteRoutine() {
        // given

        // when

        // then
        assertThatThrownBy(() -> routineService.deleteRoutine(routine.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("존재 하지 않은 루틴 삭제하기")
    void notExistRoutineDelete() {
        // given

        // when

        // then
        assertThatThrownBy(() -> routineService.deleteRoutine(2000L))
                .isInstanceOf(CommonException.class);
    }

}
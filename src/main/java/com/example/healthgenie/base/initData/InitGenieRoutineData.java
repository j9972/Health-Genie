package com.example.healthgenie.base.initData;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import static com.example.healthgenie.boundedContext.routine.entity.Level.BEGINNER;
import static com.example.healthgenie.boundedContext.routine.entity.Level.INTERMEDIATE;
import static com.example.healthgenie.boundedContext.routine.entity.Level.EXPERT;
import static com.example.healthgenie.boundedContext.routine.entity.Day.MONDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.TUESDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.WEDNESDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.THURSDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.FRIDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.SATURDAY;


@Component
@RequiredArgsConstructor
public class InitGenieRoutineData {
    private final InitGenieRoutineService initService;

    @PostConstruct
    public void init() {
        initService.dbInitLow();
        initService.dbInitMid();
        initService.dbInitHigh();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitGenieRoutineService {
        private final RoutineRepository routineRepository;

        public void dbInitLow() {
            createRoutine(BEGINNER, MONDAY, "beginner님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","스쿼트",4,12);
            createRoutine(BEGINNER, MONDAY, "beginner님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 프레스",4,12);
            createRoutine(BEGINNER, MONDAY, "beginner님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 익스텐션",4,12);
            createRoutine(BEGINNER, TUESDAY, "beginner님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","밀리터리 프레스",4,12);
            createRoutine(BEGINNER, TUESDAY, "beginner님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","사이드 레터럴 레이즈",4,12);
            createRoutine(BEGINNER, TUESDAY, "beginner님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","페이스 풀",4,15);
            createRoutine(BEGINNER, WEDNESDAY, "beginner님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","인클라인 덤벨 프레스",4,15);
            createRoutine(BEGINNER, WEDNESDAY, "beginner님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","체스트 프레스",4,15);
            createRoutine(BEGINNER, WEDNESDAY, "beginner님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두","라잉 트라이셉스 익스텐션",4,15);
            createRoutine(BEGINNER, THURSDAY, "beginner님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등","시티드 로우",4,15);
            createRoutine(BEGINNER, THURSDAY, "beginner님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등","래풀다운",4,15);
            createRoutine(BEGINNER, THURSDAY, "beginner님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "이두","덤벨 컬",4,15);

        }
        public void dbInitMid() {
            createRoutine(INTERMEDIATE, MONDAY, "intermediate님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","스쿼트",4,12);
            createRoutine(INTERMEDIATE, MONDAY, "intermediate님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 프레스",4,12);
            createRoutine(INTERMEDIATE, MONDAY, "intermediate님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 익스텐션",4,12);
            createRoutine(INTERMEDIATE, TUESDAY, "intermediate님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","밀리터리 프레스",4,12);
            createRoutine(INTERMEDIATE, TUESDAY, "intermediate님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","사이드 레터럴 레이즈",4,12);
            createRoutine(INTERMEDIATE, TUESDAY, "intermediate님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","페이스 풀",4,15);
            createRoutine(INTERMEDIATE, WEDNESDAY, "intermediate님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","인클라인 덤벨 프레스",4,15);
            createRoutine(INTERMEDIATE, WEDNESDAY, "intermediate님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","체스트 프레스",4,15);
            createRoutine(INTERMEDIATE, WEDNESDAY, "intermediate님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두","라잉 트라이셉스 익스텐션",4,15);
            createRoutine(INTERMEDIATE, THURSDAY, "intermediate님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등","시티드 로우",4,15);
            createRoutine(INTERMEDIATE, THURSDAY, "intermediate님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등","래풀다운",4,15);
            createRoutine(INTERMEDIATE, THURSDAY, "intermediate님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "이두","덤벨 컬",4,15);
            createRoutine(INTERMEDIATE, FRIDAY, "intermediate님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어깨 후면","팩덱 플라이",4,15);
            createRoutine(INTERMEDIATE, FRIDAY, "intermediate님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "윗 가슴","인클라인 바벨 프레스",4,15);
            createRoutine(INTERMEDIATE, FRIDAY, "intermediate님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "윗 가슴","케이블",4,15);
        }
        public void dbInitHigh() {
            createRoutine(EXPERT, MONDAY, "expert님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","스쿼트",4,12);
            createRoutine(EXPERT, MONDAY, "expert님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 프레스",4,12);
            createRoutine(EXPERT, MONDAY, "expert님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 익스텐션",4,12);
            createRoutine(EXPERT, TUESDAY, "expert님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","밀리터리 프레스",4,12);
            createRoutine(EXPERT, TUESDAY, "expert님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","사이드 레터럴 레이즈",4,12);
            createRoutine(EXPERT, TUESDAY, "expert님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","페이스 풀",4,15);
            createRoutine(EXPERT, WEDNESDAY, "expert님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","인클라인 덤벨 프레스",4,15);
            createRoutine(EXPERT, WEDNESDAY, "expert님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","체스트 프레스",4,15);
            createRoutine(EXPERT, WEDNESDAY, "expert님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두","라잉 트라이셉스 익스텐션",4,15);
            createRoutine(EXPERT, THURSDAY, "expert님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등","시티드 로우",4,15);
            createRoutine(EXPERT, THURSDAY, "expert님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등","래풀다운",4,15);
            createRoutine(EXPERT, THURSDAY, "expert님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "이두","덤벨 컬",4,15);
            createRoutine(EXPERT, FRIDAY, "expert님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어깨 후면","팩덱 플라이",4,15);
            createRoutine(EXPERT, FRIDAY, "expert님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "윗 가슴","인클라인 바벨 프레스",4,15);
            createRoutine(EXPERT, FRIDAY, "expert님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "윗 가슴","케이블",4,15);
            createRoutine(EXPERT, SATURDAY, "expert님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두","덤벨 오버헤드 익스텐션",4,15);
            createRoutine(EXPERT, SATURDAY, "expert님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "이두","바벨 컬",4,15);
            createRoutine(EXPERT, SATURDAY, "expert님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "이두","헤머컬",4,15);

        }


        private Routine createRoutine(Level level, Day day, String content,String part, String workoutName, int sets, int reps) {
            WorkoutRecipe recipe = new WorkoutRecipe(part, workoutName, sets, reps);
            Routine routine = Routine.builder()
                    .level(level)
                    .day(day)
                    .content(content)
                    .workoutRecipe(recipe)
                    .build();

            return routineRepository.save(routine);
        }
    }
}
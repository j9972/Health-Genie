package com.example.healthgenie.base.initData;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.GenieRoutine;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.repository.GenieRoutineRepository;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        private final GenieRoutineRepository routineRepository;

        public void dbInitLow() {
            createRoutine(BEGINNER, Collections.singletonList("하체"), MONDAY,"beginner님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("스쿼트","4","12"));
            createRoutine(BEGINNER, Collections.singletonList("하체"), MONDAY,"beginner님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("레그 프레스","4","12"));
            createRoutine(BEGINNER, Collections.singletonList("하체"), MONDAY,"beginner님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("레그 익스텐션","4","12"));
            createRoutine(BEGINNER, Collections.singletonList("어깨"), TUESDAY,"beginner님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("밀리터리 프레스","4","12"));
            createRoutine(BEGINNER, Collections.singletonList("어깨"), TUESDAY,"beginner님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("사이드 레터럴 레이즈","4","12"));
            createRoutine(BEGINNER, Collections.singletonList("어깨"), TUESDAY,"beginner님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("페이스 풀","4","15"));
            createRoutine(BEGINNER, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"beginner님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("인클라인 덤벨 프레스","4","15"));
            createRoutine(BEGINNER, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"beginner님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("체스트 프레스","4","15"));
            createRoutine(BEGINNER, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"beginner님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("라잉 트라이셉스 익스텐션","4","15"));
            createRoutine(BEGINNER, Collections.singletonList("등, 이두"), THURSDAY,"beginner님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("시티드 로우","4","15"));
            createRoutine(BEGINNER, Collections.singletonList("등, 이두"), THURSDAY,"beginner님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("래풀다운","4","15"));
            createRoutine(BEGINNER, Collections.singletonList("등, 이두"), THURSDAY,"beginner님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("덤벨 ","4","15"));
        }
        public void dbInitMid() {
            createRoutine(INTERMEDIATE, Collections.singletonList("하체"), MONDAY,"intermediate님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("스쿼트","4","12"));
            createRoutine(INTERMEDIATE, Collections.singletonList("하체"), MONDAY,"intermediate님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("레그 프레스","4","12"));
            createRoutine(INTERMEDIATE, Collections.singletonList("하체"), MONDAY,"intermediate님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("레그 익스텐션","4","12"));
            createRoutine(INTERMEDIATE, Collections.singletonList("어깨"), TUESDAY,"intermediate님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("밀리터리 프레스","4","12"));
            createRoutine(INTERMEDIATE, Collections.singletonList("어깨"), TUESDAY,"intermediate님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("사이드 레터럴 레이즈","4","12"));
            createRoutine(INTERMEDIATE, Collections.singletonList("어깨"), TUESDAY,"intermediate님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("페이스 풀","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"intermediate님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("인클라인 덤벨 프레스","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"intermediate님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("체스트 프레스","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"intermediate님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("라잉 트라이셉스 익스텐션","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("등, 이두"), THURSDAY,"intermediate님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("시티드 로우","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("등, 이두"), THURSDAY,"intermediate님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("래풀다운","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("등, 이두"), THURSDAY,"intermediate님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("덤벨 컬","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("어깨 후면, 가슴 윗면"), FRIDAY,"intermediate님 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", Arrays.asList("팩덱 플라이","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("어깨 후면, 가슴 윗면"), FRIDAY,"intermediate님 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", Arrays.asList("인클라인 바벨 프레스","4","15"));
            createRoutine(INTERMEDIATE, Collections.singletonList("어깨 후면, 가슴 윗면"), FRIDAY,"intermediate님 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", Arrays.asList("케이블","4","15"));
        }
        public void dbInitHigh() {
            createRoutine(EXPERT, Collections.singletonList("하체"), MONDAY,"expert님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("스쿼트","4","12"));
            createRoutine(EXPERT, Collections.singletonList("하체"), MONDAY,"expert님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("레그 프레스","4","12"));
            createRoutine(EXPERT, Collections.singletonList("하체"), MONDAY,"expert님! 오늘은 하체 데이입니다! 화아팅 하세요", Arrays.asList("레그 익스텐션","4","12"));
            createRoutine(EXPERT, Collections.singletonList("어깨"), TUESDAY,"expert님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("밀리터리 프레스","4","12"));
            createRoutine(EXPERT, Collections.singletonList("어깨"), TUESDAY,"expert님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("사이드 레터럴 레이즈","4","12"));
            createRoutine(EXPERT, Collections.singletonList("어깨"), TUESDAY,"expert님! 오늘은 어깨 데이입니다! 화아팅 하세요", Arrays.asList("페이스 풀","4","15"));
            createRoutine(EXPERT, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"expert님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("인클라인 덤벨 프레스","4","15"));
            createRoutine(EXPERT, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"expert님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("체스트 프레스","4","15"));
            createRoutine(EXPERT, Collections.singletonList("가슴, 삼두"), WEDNESDAY,"expert님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요",  Arrays.asList("라잉 트라이셉스 익스텐션","4","15"));
            createRoutine(EXPERT, Collections.singletonList("등, 이두"), THURSDAY,"expert님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("시티드 로우","4","15"));
            createRoutine(EXPERT, Collections.singletonList("등, 이두"), THURSDAY,"expert님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("래풀다운","4","15"));
            createRoutine(EXPERT, Collections.singletonList("등, 이두"), THURSDAY,"expert님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", Arrays.asList("덤벨 컬","4","15"));
            createRoutine(EXPERT, Collections.singletonList("어깨 후면, 가슴 윗면"), FRIDAY,"expert님 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", Arrays.asList("팩덱 플라이","4","15"));
            createRoutine(EXPERT, Collections.singletonList("어깨 후면, 가슴 윗면"), FRIDAY,"expert님 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", Arrays.asList("인클라인 바벨 프레스","4","15"));
            createRoutine(EXPERT, Collections.singletonList("어깨 후면, 가슴 윗면"), FRIDAY,"expert님 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", Arrays.asList("케이블","4","15"));
            createRoutine(EXPERT, Collections.singletonList("삼두 이두"), SATURDAY,"expert님 오늘은 삼두 이두 슈퍼 세트 데이입니다! 화아팅 하세요", Arrays.asList("바벨 컬","4","15"));
            createRoutine(EXPERT, Collections.singletonList("삼두 이두"), SATURDAY,"expert님 오늘은 삼두 이두 슈퍼 세트 데이입니다! 화아팅 하세요", Arrays.asList("덤벨 오버헤드 익스텐션","4","15"));
            createRoutine(EXPERT, Collections.singletonList("삼두 이두"), SATURDAY,"expert님 오늘은 삼두 이두 슈퍼 세트 데이입니다! 화아팅 하세요", Arrays.asList("헤머컬","4","15"));

        }


        private GenieRoutine createRoutine(Level level, List<String> parts, Day day, String content,List<String> workout) {
            GenieRoutine routine = GenieRoutine.builder()
                    .level(level)
                    .parts(parts)
                    .day(day)
                    .content(content)
                    .workout(workout)
                    .build();

            return routineRepository.save(routine);
        }
    }
}
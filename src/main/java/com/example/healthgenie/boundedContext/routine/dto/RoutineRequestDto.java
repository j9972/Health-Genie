package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineRequestDto {
    private Long id;
    private Level level; // genie에서 사용되는 난이도
    private Day day;
    private String content;
    private List<String> parts;
    private String workoutName; // 운동 명칭
    private int kg;
    private int sets;
    private int reps;
    private String writer; // 헬스 루틴 작성자
}

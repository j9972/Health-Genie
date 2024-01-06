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
    private Day day;
    private String parts;
    private List<WorkoutRecipe> workoutRecipe;
    private String writer; // 헬스 루틴 작성자 닉네임
}

package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Routine toEntity(User user, WorkoutRecipe recipe) {
        return Routine.builder()
                .day(this.day)
                .parts(this.parts)
                .workoutRecipe(recipe)
                .member(user)
                .build();
    }
}

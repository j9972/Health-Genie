package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RoutineUpdateRequestDto {
    private Long id;
    private Day day;
    private String parts;
    private List<WorkoutRecipe> workoutRecipe;
    public boolean hasDay(){
        return day != null;
    }
    public boolean hasParts(){
        return parts != null;
    }
    public boolean hasWorkoutRecipe(){
        return workoutRecipe != null;
    }
}

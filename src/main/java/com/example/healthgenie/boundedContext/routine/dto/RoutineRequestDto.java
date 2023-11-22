package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import lombok.Data;

@Data
public class RoutineRequestDto {
    private Long id;
    private Level level; // own 에서는 사용 안함
    private String workoutPart;
    private String workoutName;
    private String workoutDay;
    private int workoutSets;
    private int workoutReps;
    private Long userId; // genie 에서는 사용 안함
}
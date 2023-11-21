package com.example.healthgenie.domain.routine.dto;

import com.example.healthgenie.domain.routine.entity.GenieRoutine;
import com.example.healthgenie.domain.routine.entity.Level;
import com.example.healthgenie.domain.routine.entity.OwnRoutine;
import com.example.healthgenie.domain.todo.entity.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoutineResponseDto {
    private Long id;
    private Level level;
    private String workoutPart;
    private String workoutName;
    private String workoutDay;
    private int workoutSets;
    private int workoutReps;
    private Long userId;


    public static RoutineResponseDto of(GenieRoutine genie) {
        return RoutineResponseDto.builder()
                .id(genie.getId())
                .level(Level.valueOf(genie.getLevel())) // .level(genie.getLevel())
                .workoutPart(genie.getWorkoutPart())
                .workoutName(genie.getName())
                .workoutDay(genie.getWorkoutDay())
                .workoutSets(genie.getWorkoutSets())
                .workoutReps(genie.getWorkoutReps())
                .build();
    }

    public static RoutineResponseDto of(OwnRoutine own) {
        return RoutineResponseDto.builder()
                .id(own.getId())
                .workoutPart(own.getWorkoutPart())
                .workoutName(own.getName())
                .workoutDay(own.getWorkoutDay())
                .workoutSets(own.getWorkoutSets())
                .workoutReps(own.getWorkoutReps())
                .userId(own.getMember().getId())
                .build();
    }
}

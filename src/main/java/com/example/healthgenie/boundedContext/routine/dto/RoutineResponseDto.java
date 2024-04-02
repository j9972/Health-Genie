package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineResponseDto {
    private Long id;
    private Day day;
    private String content;
    private String parts;
    private WorkoutRecipe recipe; // workoutName, sets reps

    public static RoutineResponseDto ofOwn(Routine routine) {
        return RoutineResponseDto.builder()
                .id(routine.getId())
                .day(routine.getDay())
                .parts(routine.getParts())
                .recipe(routine.getWorkoutRecipe())
                .build();
    }

    public static List<RoutineResponseDto> ofOwn(List<Routine> routine) {
        return routine.stream()
                .map(RoutineResponseDto::ofOwn)
                .collect(toList());
    }

    public static RoutineResponseDto ofGenie(Routine routine) {
        return RoutineResponseDto.builder()
                .id(routine.getId())
                .day(routine.getDay())
                .content(routine.getContent())
                .parts(routine.getParts())
                .recipe(routine.getWorkoutRecipe())
                .build();
    }

    public static List<RoutineResponseDto> ofGenie(List<Routine> genie) {
        return genie.stream()
                .map(RoutineResponseDto::ofGenie)
                .collect(toList());
    }

}

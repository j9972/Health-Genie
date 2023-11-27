package com.example.healthgenie.boundedContext.routine.dto;


import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.GenieRoutine;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GenieResponseDto {
    private Long id;
    private Level level; // genie에서 사용되는 난이도
    private Day day;
    private List<String> parts;
    private String content;
    private List<String> workout;

    public static GenieResponseDto ofGenie(GenieRoutine routine) {
        return GenieResponseDto.builder()
                .id(routine.getId())
                .level(routine.getLevel())
                .day(routine.getDay())
                .parts(routine.getParts())
                .content(routine.getContent())
                .workout(routine.getWorkout())
                .build();
    }
}

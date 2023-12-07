package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.routine.entity.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
public class RoutineResponseDto {
    private Long id;
    private Level level;
    private Day day;
    private String content;
    private List<String> parts;
    private WorkoutRecipe recipe; // woroutName, sets reps
    private String writer; // 헬스 루틴 작성자 -> genie or 본인 이름

    public static RoutineResponseDto ofOwn(Routine routine) {
        return RoutineResponseDto.builder()
                .id(routine.getId())
                .level(routine.getLevel())
                .day(routine.getDay())
                .content(routine.getContent())
                .parts(routine.getPart())
                .recipe(routine.getWorkoutRecipe())
                .writer(routine.getMember().getNickname())
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
                .level(routine.getLevel())
                .day(routine.getDay())
                .content(routine.getContent())
                .parts(routine.getPart())
                .recipe(routine.getWorkoutRecipe())
                .build();
    }

    public static List<RoutineResponseDto> ofGenie(List<Routine> genie) {
        return genie.stream()
                .map(RoutineResponseDto::ofGenie)
                .collect(toList());
    }
}

package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoutineResponseDto {
    private Long id;
    private Level level; // genie에서 사용되는 난이도
    private List<String> parts;
    private Day day;
    private String content;
    private String workoutName; // 운동 명칭
    private int sets;
    private int reps;
    private String writer; // 헬스 루틴 작성자 -> genie or 본인 이름

    public static RoutineResponseDto ofOwn(Routine routine) {
        return RoutineResponseDto.builder()
                .id(routine.getId())
                .parts(routine.getParts())
                .day(routine.getDay())
                .content(routine.getContent())
                .workoutName(routine.getWorkoutName())
                .sets(routine.getSets())
                .reps(routine.getReps())
                .writer(routine.getMember().getEmail())
                .build();
    }
}

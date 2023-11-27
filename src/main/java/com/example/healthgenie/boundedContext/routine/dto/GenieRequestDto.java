package com.example.healthgenie.boundedContext.routine.dto;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import lombok.Data;

import java.util.List;

@Data
public class GenieRequestDto {
    private Long id;
    private Level level; // genie에서 사용되는 난이도
    private List<String> parts;
    private Day day;
    private String content;
    private List<String> workout;
}

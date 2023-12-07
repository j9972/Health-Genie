package com.example.healthgenie.boundedContext.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingCondition {

    private String date;
    private String time;
    private Long userId;
    private Long trainerId;
}

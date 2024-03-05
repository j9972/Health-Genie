package com.example.healthgenie.boundedContext.matching.dto;

import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingRequest {

    private String date;
    private String time;
    private String place;
    private String description;
    private MatchingState state;
    private Long userId;
    private Long trainerId;
}

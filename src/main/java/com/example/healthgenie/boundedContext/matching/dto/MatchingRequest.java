package com.example.healthgenie.boundedContext.matching.dto;

import com.example.healthgenie.boundedContext.matching.entity.enums.MatchingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchingRequest {

    private LocalDateTime date;
    private String place;
    private String description;
    private MatchingState state;
    private Long userId;
}

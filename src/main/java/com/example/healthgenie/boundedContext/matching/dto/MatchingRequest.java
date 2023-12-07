package com.example.healthgenie.boundedContext.matching.dto;

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
    private String userNickname;
    private String trainerNickname;
}

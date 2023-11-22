package com.example.healthgenie.boundedContext.ptreview.dto;

import lombok.*;

@Data
public class PtReviewRequestDto {
    private Long id;

    private String content;

    private String stopReason;

    private Double reviewScore;

    private Long trainerId;

    private Long userId;
}

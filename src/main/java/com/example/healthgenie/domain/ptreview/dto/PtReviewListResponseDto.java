package com.example.healthgenie.domain.ptreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtReviewListResponseDto {
    private Long id;

    private String content;

    private String stopReason;

    private Double reviewScore;

    private Long trainerId;

    private Long userId;
}

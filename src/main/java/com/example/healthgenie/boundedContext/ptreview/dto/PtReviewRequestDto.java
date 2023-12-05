package com.example.healthgenie.boundedContext.ptreview.dto;

import lombok.*;

@Data
public class PtReviewRequestDto {
    private Long id;

    private String content;

    private String stopReason;

    private Double reviewScore;

    private String userNickName;
    private String trainerNickName; // 작성하는 사람
}

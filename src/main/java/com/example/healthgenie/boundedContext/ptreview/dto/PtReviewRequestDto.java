package com.example.healthgenie.boundedContext.ptreview.dto;

import lombok.*;

@Data
public class PtReviewRequestDto {
    private Long id;

    private String content;

    private String stopReason;

    private Double reviewScore;

    private String userMail;// 작성하는 사람
    private String trainerMail;
}

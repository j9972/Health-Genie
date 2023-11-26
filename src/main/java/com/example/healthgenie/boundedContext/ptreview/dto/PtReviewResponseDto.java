package com.example.healthgenie.boundedContext.ptreview.dto;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import lombok.*;

@Data
@Builder
public class PtReviewResponseDto {
    private Long id;

    private String content;

    private String stopReason;

    private Double reviewScore;


    private String userMail;// 작성하는 사람
    private String trainerMail;

    public static PtReviewResponseDto of(PtReview review) {
        return PtReviewResponseDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .stopReason(review.getStopReason())
                .reviewScore(review.getReviewScore())
                .trainerMail(review.getTrainer().getEmail())
                .userMail(review.getMember().getEmail())
                .build();
    }
}

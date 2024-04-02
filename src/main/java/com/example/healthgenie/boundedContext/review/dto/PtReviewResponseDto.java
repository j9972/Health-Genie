package com.example.healthgenie.boundedContext.review.dto;

import com.example.healthgenie.boundedContext.review.entity.PtReview;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PtReviewResponseDto {
    private Long id;
    private String content;
    private String stopReason;
    private Double reviewScore;
    private String userName;
    private String trainerName; // 작성하는 사람
    private LocalDateTime createdAt;

    public static PtReviewResponseDto of(PtReview review) {
        return PtReviewResponseDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .stopReason(review.getStopReason())
                .reviewScore(review.getReviewScore())
                .trainerName(review.getTrainer().getName())
                .userName(review.getMember().getName())
                .createdAt(review.getCreatedDate())
                .build();
    }

    public static List<PtReviewResponseDto> of(List<PtReview> review) {
        return review.stream()
                .map(PtReviewResponseDto::of)
                .toList();
    }
}

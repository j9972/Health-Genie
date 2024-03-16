package com.example.healthgenie.boundedContext.ptreview.dto;

import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
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
    private String userNickName;
    private String trainerNickName; // 작성하는 사람

    public static PtReviewResponseDto of(PtReview review) {
        return PtReviewResponseDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .stopReason(review.getStopReason())
                .reviewScore(review.getReviewScore())
                .trainerNickName(review.getTrainer().getNickname())
                .userNickName(review.getMember().getNickname())
                .build();
    }

    public static List<PtReviewResponseDto> of(List<PtReview> review) {
        return review.stream()
                .map(PtReviewResponseDto::of)
                .toList();
    }
}

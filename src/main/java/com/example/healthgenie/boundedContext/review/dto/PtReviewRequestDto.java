package com.example.healthgenie.boundedContext.review.dto;

import com.example.healthgenie.boundedContext.review.entity.PtReview;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PtReviewRequestDto {
    private Long id;

    private String content;

    private String stopReason;

    private Double reviewScore;

    private Long userId;

    private Long trainerId; // 작성하는 사람

    public PtReview toEntity(User trainer, User currentUser) {
        return PtReview.builder()
                .stopReason(this.stopReason)
                .reviewScore(this.reviewScore)
                .content(this.content)
                .member(currentUser)
                .trainer(trainer)
                .build();
    }
}

package com.example.healthgenie.boundedContext.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PtReviewUpdateRequest {
    private String content;

    private String stopReason;

    private Double reviewScore;

    public boolean hasContent() {
        return content != null;
    }

    public boolean hasStopReason() {
        return stopReason != null;
    }

    public boolean hasReviewScore() {
        return reviewScore != null;
    }
}

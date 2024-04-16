package com.example.healthgenie.boundedContext.ptreview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PtReviewUpdateRequest {
    private String content;

    private String stopReason;

    private Double reviewScore;

    public boolean hasContent(){
        return content != null;
    }
    public boolean hasStopReason(){
        return stopReason != null;
    }
    public boolean hasReviewScore(){
        return reviewScore != null;
    }
}

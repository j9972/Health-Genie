package com.example.healthgenie.domain.ptreview.dto;

import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtReviewDetailResponseDto {

    private Long id;

    private String title;

    private String startDate;

    private String endDate;

    private String trainerName;

    private String reviewContent;

    private Blob pic1;

    private Blob pic2;

    private Blob pic3;

    private int starScore;

    private Long trainerId;
}

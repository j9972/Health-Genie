package com.example.healthgenie.domain.ptrecord.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtProcessDetailResponseDto {

    private Long id;

    private Long ptTimes;

    private String date;

    private String ptStartDate;

    private String ptEndTimes;

    private String ptComment;

    private String bodyState;

    private String bmi;

    private String weakness;

    private String strength;

    private Long trainerId;

    private Long userId;
}
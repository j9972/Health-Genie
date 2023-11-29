package com.example.healthgenie.boundedContext.trainer.dto;

import lombok.Data;

@Data
/*
    트레이너 전용 - 관리페이지에 있어야 하는 내용
 */
public class ProfileRequestDto {
    private Long id;
    private String introduction;
    private String career;
    private int cost;
    private int month; // 견적을 개월 수로 변환
    private String email; // 트레이너 이메일
}

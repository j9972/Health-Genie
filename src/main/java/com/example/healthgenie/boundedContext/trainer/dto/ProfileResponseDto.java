package com.example.healthgenie.boundedContext.trainer.dto;

import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
/*
    트레이너 전용 - 관리페이지에 있어야 하는 내용
 */
public class ProfileResponseDto {
    private Long id;
    private String introduction;
    private String career;
    private int cost;
    private int month; // 견적을 개월 수로 변환
    private String nickname; // 트레이너 닉네임

    public static ProfileResponseDto ofProfile(TrainerInfo profile) {
        return ProfileResponseDto.builder()
                .id(profile.getId())
                .introduction(profile.getIntroduction())
                .career(profile.getCareer())
                .cost(profile.getCost())
                .month(profile.getCareerMonth())
                .nickname(profile.getMember().getNickname())
                .build();
    }
}

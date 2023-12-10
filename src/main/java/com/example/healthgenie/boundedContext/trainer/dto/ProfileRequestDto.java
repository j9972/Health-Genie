package com.example.healthgenie.boundedContext.trainer.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
/*
    트레이너 전용 - 관리페이지에 있어야 하는 내용
 */
public class ProfileRequestDto {
    private Long id;
    private String introduction;
    private String career;
    private int cost;
    private int month; // 견적을 개월 수로 변환
    private String university;
    private LocalTime startTime; // contact 가능한 시간 시작
    private LocalTime endTime; // contact 가능한 시간 끝
    private Double reviewAvg;
    private String nickname; // 트레이너 닉네임
    private List<MultipartFile> photos; // 트레이너 사진들
}

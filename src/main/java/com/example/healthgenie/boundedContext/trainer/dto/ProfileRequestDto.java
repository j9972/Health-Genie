package com.example.healthgenie.boundedContext.trainer.dto;

import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
    트레이너 전용 - 관리페이지에 있어야 하는 내용
 */
public class ProfileRequestDto {
    private Long id;
    private String introduction;
    private String career;
    private int cost;
    private int month; // 견적을 개월 수로 변환
    private String name;
    private String university;
    private LocalTime startTime; // contact 가능한 시간 시작
    private LocalTime endTime; // contact 가능한 시간 끝
    private Double reviewAvg;
    private String nickname; // 트레이너 닉네임
    private List<MultipartFile> photos; // 트레이너 사진들

    public TrainerInfo toEntity(User user) {
        return TrainerInfo.builder()
                .introduction(this.introduction)
                .career(this.career)
                .careerMonth(this.month)
                .cost(this.cost)
                .name(this.name)
                .university(this.university)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .reviewAvg(this.reviewAvg)
                .name(this.name)
                .member(user)
                .build();

    }

    public boolean hasIntroduction() {
        return introduction != null;
    }

    public boolean hasCareer() {
        return career != null;
    }

    public boolean hasCost() {
        return cost != 0;
    }

    public boolean hasMonth() {
        return month != 0;
    }

    public boolean hasStartTime() {
        return startTime != null;
    }

    public boolean hasEndTime() {
        return endTime != null;
    }

    public boolean hasUniversity() {
        return university != null;
    }

    public boolean hasReviewAvg() {
        return reviewAvg != null;
    }
}

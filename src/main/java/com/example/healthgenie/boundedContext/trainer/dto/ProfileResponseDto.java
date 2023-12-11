package com.example.healthgenie.boundedContext.trainer.dto;

import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineResponseDto;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.service.TrainerProfileService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String name;
    private String university;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double reviewAvg;
    private List<String> photoPaths;

    public static ProfileResponseDto of(TrainerInfo info) {
        List<String> paths = new ArrayList<>();
        if(info.getTrainerPhotos() != null) {
            paths = info.getTrainerPhotos().stream()
                    .map(TrainerPhoto::getInfoPhotoPath)
                    .toList();
        }


        return ProfileResponseDto.builder()
                .id(info.getId())
                .introduction(info.getIntroduction())
                .career(info.getCareer())
                .month(info.getCareerMonth())
                .cost(info.getCost())
                .university(info.getUniversity())
                .startTime(info.getStartTime())
                .endTime(info.getEndTime())
                .reviewAvg(info.getReviewAvg())
                .name(info.getName())
                .nickname(info.getMember().getNickname())
                .photoPaths(paths)
                .build();
    }

    public static List<ProfileResponseDto> of(List<TrainerInfo> infos) {
        return infos.stream()
                .map(ProfileResponseDto::of)
                .toList();
    }
}

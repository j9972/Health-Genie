package com.example.healthgenie.domain.trainer.dto;

import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerSimpleInfo {

    private Long id;

    private String name;

    private String description;

    private String uniname;
    // google이나 이런데에서 처음에 어떻게 사진을 가져올 지 몰라서 혹시 몰라 만든 Col
    private String profilePhoto;

    private Double reviewAvg;

    public Page<TrainerSimpleInfo> toDtoPage(Page<TrainerInfo> list){
        Page<TrainerSimpleInfo> boardDtoList = list.map(m -> TrainerSimpleInfo.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .uniname(m.getUniname())
                .profilePhoto(m.getProfilePhoto())
                .reviewAvg(m.getReviewAvg())
                .build());
        return boardDtoList;
    }

}

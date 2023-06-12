package com.example.healthgenie.domain.ptreview.dto;

import com.example.healthgenie.domain.user.entity.Role;
import lombok.*;

import java.sql.Blob;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PtReviewRequestDto {

    //필요한정보 매칭내역 즉 트레이너 이름 아이디,역할, 유저아이디, 매칭아이디
    private Role role;

    private Long trainerId;

    private Long MatchingId;

    private String trainerName;

    private String startDate;

    private String endDate;

    private String title;

    private String Contents;

    private Blob pic1;

    private Blob pic2;

    private Blob pic3;

    private int starScore;
}

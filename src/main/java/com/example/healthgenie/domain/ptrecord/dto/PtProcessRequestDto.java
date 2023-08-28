package com.example.healthgenie.domain.ptrecord.dto;

import com.example.healthgenie.domain.user.entity.Role;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PtProcessRequestDto {

    // 회원 id,  트레이너 id 도 필요하다
    private Role role;

    private Long trainerId;

    private Long userId;

    private String date;

    private Long ptTimes;

    private String bodyState;

    private String bmi;

    private String weakness;

    private String strength;

    private String ptComment;

    private String ptStartDate;

    private String ptEndTimes;
}

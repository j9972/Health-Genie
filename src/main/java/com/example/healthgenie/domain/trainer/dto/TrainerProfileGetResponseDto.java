package com.example.healthgenie.domain.trainer.dto;

import com.example.healthgenie.domain.user.entity.User;
import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileGetResponseDto {

    private Long id;

    private String name;

    private String description;

    private String prize;

    private String certification;

    private Blob pics;

    private Long matchingTimes;

    private Long avgSarScore;

    private User trainer;


}

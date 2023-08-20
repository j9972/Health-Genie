package com.example.healthgenie.domain.trainer.dto;

import lombok.*;

import java.sql.Blob;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TrainerProfileModifyRequestDto {

    private Long profileId;

    private String description;

    private String prize;

    private String certification;

    private String pics;


}

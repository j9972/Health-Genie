package com.example.healthgenie.domain.trainer.dto;

import lombok.*;

import java.sql.Blob;



@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TrainerProfileRequestDto {

    private String name;

    private String description;

    private String prize;

    private String certification;

    private Blob pics;

    private Long matchingTimes;

    private Long avgSarScore;
}

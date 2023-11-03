package com.example.healthgenie.domain.trainer.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileRequestDto {
    private Long id;
    private String name;
    private int starttime;
    private int endtime;
    private String description;
    private int month;
    private float reviewAvg;
    private Long member;
    private int price;
    private String uniName;
    private String intro;
}

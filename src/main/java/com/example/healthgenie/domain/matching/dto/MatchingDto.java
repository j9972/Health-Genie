package com.example.healthgenie.domain.matching.dto;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class MatchingDto {
    private Long id;
    private Date ptDate;
    private String ptPlace;
    private boolean ptAccept;
    private int price;
    private boolean ptExperience;
    private Long trainerId;
    private Long userId;
}

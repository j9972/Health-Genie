package com.example.healthgenie.boundedContext.matching.dto;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class MatchingDto {
    private Long id;
    private Date date;
    private String place;
    private String time;
    private boolean ptAccept;
    private int price;
    private boolean experience;
    private Long trainerId;
    private Long userId;
}

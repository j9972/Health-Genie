package com.example.healthgenie.domain.ptreview.dto;



import lombok.*;

import java.sql.Blob;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PtReviewListResponseDto {

    private Long id;

    private String title;

    private String startDate;

    private String endDate;

    private String reviewContent;

    private Blob pic1;
    private Blob pic2;
    private Blob pic3;

    private int starScore;


}

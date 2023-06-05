package com.example.healthgenie.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostListResponseDto {


    private Long totalCnt;

    private int totalPages;

    private Pageable pageable;



    List<CommunityPostIdTitleDto> postList;
}

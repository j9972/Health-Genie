package com.example.healthgenie.domain.community.dto;

import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostRequestDto {

    private String title;

    private String body;

    private Long likeCount;

    private Blob pics;


}

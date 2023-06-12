package com.example.healthgenie.domain.community.dto;

import com.example.healthgenie.domain.community.entity.CommunityComment;
import lombok.*;

import java.sql.Blob;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostGetResponseDto {

    private Long id;

    private String title;

    private String body;

    private Long likeCount;

    private Blob pics;

    private List<CommunityComment> commentList;
}

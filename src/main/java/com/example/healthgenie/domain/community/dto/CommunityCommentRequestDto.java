package com.example.healthgenie.domain.community.dto;

import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentRequestDto {
    private String commentBody;

    private Long likeCount;
}

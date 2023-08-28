package com.example.healthgenie.domain.community.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCommentGetResponseDto {

    private Long id;

    private String commentBody;

    private Long likeCount;
}

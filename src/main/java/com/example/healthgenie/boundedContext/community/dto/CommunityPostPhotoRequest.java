package com.example.healthgenie.boundedContext.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityPostPhotoRequest {

    private Long postId;
    private String photoPath;
}

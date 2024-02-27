package com.example.healthgenie.boundedContext.community.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoRequest {

    private Long postId;
    private String photoPath;
}

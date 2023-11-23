package com.example.healthgenie.boundedContext.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {

    private Long id;
    private Long postId;
    private String content;
    private Long userId;
}

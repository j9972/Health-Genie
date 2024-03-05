package com.example.healthgenie.boundedContext.community.like.dto;

import com.example.healthgenie.boundedContext.community.like.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {

    private Long likeId;
    private Long postId;
    private Long userId;

    public static LikeResponse of(Like like) {
        return LikeResponse.builder()
                .likeId(like.getId())
                .postId(like.getPost().getId())
                .userId(like.getUser().getId())
                .build();
    }
}

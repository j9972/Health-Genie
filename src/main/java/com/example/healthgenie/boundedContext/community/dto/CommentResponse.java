package com.example.healthgenie.boundedContext.community.dto;

import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long userId;
    private String content;

    public static CommentResponse of(CommentRequest request) {
        return CommentResponse.builder()
                .id(request.getId())
                .postId(request.getPostId())
                .userId(request.getUserId())
                .content(request.getContent())
                .build();
    }

    public static CommentResponse of(CommunityComment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getMember().getId())
                .content(comment.getCommentBody())
                .build();
    }

    public static List<CommentResponse> of(List<CommunityComment> comments) {
        return comments.stream()
                .map(CommentResponse::of)
                .toList();
    }
}

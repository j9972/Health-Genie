package com.example.healthgenie.boundedContext.community.dto;

import com.example.healthgenie.boundedContext.community.entity.CommunityComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentResponse {

    private Long id;
    private LocalDateTime createdDate;
    private String writer;
    private String content;

    public static CommentResponse of(CommunityComment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .createdDate(comment.getCreatedDate())
                .writer(comment.getMember().getName())
                .content(comment.getCommentBody())
                .build();
    }

    public static List<CommentResponse> of(List<CommunityComment> comments) {
        return comments.stream()
                .map(CommentResponse::of)
                .toList();
    }
}

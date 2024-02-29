package com.example.healthgenie.boundedContext.community.comment.dto;

import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
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
    private LocalDateTime lastModifiedDate;
    private String writer;
    private String content;
    private String profilePhoto;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .createdDate(comment.getCreatedDate())
                .lastModifiedDate(comment.getLastModifiedDate())
                .writer(comment.getWriter().getNickname())
                .content(comment.getCommentBody())
                .build();
    }

    public static List<CommentResponse> of(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponse::of)
                .toList();
    }
}

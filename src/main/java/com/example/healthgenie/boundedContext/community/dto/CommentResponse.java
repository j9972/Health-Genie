package com.example.healthgenie.boundedContext.community.dto;

import com.example.healthgenie.base.utils.DateUtils;
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
    private String date;
    private String time;
    private String writer;
    private String content;

    public static CommentResponse of(CommunityComment comment) {
        LocalDateTime dateTime = comment.getCreatedDate();
        String date = DateUtils.toDate(dateTime);
        String time = DateUtils.toTime(dateTime);

        return CommentResponse.builder()
                .id(comment.getId())
                .date(date)
                .time(time)
                .writer(comment.getWriter().getNickname())
                .content(comment.getCommentBody())
                .build();
    }

    public static List<CommentResponse> of(List<CommunityComment> comments) {
        return comments.stream()
                .map(CommentResponse::of)
                .toList();
    }
}

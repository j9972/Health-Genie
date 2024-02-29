package com.example.healthgenie.boundedContext.community.post.dto;

import com.example.healthgenie.boundedContext.community.post.entity.Post;
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
public class PostResponse {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String title;
    private String content;
    private String writer;
    private String writerPhoto;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getWriter().getNickname())
                .build();
    }


    public static List<PostResponse> of(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }
}
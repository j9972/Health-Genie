package com.example.healthgenie.boundedContext.community.dto;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.entity.CommunityPostPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PostResponse {

    private Long id;
    private LocalDateTime createdDate;
    private String title;
    private String content;
    private String writer;
    private List<String> photoPaths;
    private List<CommentResponse> comments;

    public static PostResponse of(CommunityPost post) {
        List<String> paths = new ArrayList<>();
        if(post.getCommunityPostPhotos() != null) {
            paths = post.getCommunityPostPhotos().stream()
                    .map(CommunityPostPhoto::getPostPhotoPath)
                    .toList();
        }

        List<CommentResponse> comments = post.getCommunityComments().stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .createdDate(comment.getCreatedDate())
                        .content(comment.getCommentBody())
                        .writer(comment.getMember().getName())
                        .build())
                .toList();

        return PostResponse.builder()
                .id(post.getId())
                .createdDate(post.getCreatedDate())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getMember().getName())
                .photoPaths(paths)
                .comments(comments)
                .build();
    }

    public static List<PostResponse> of(List<CommunityPost> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }
}
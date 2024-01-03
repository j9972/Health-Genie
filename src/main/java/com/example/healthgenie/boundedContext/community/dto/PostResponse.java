package com.example.healthgenie.boundedContext.community.dto;

import com.example.healthgenie.base.utils.DateUtils;
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
    private String date;
    private String time;
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
                .map(comment -> {
                    LocalDateTime dateTime = comment.getCreatedDate();
                    return CommentResponse.builder()
                            .id(comment.getId())
                            .date(DateUtils.toStringDate(dateTime))
                            .time(DateUtils.toStringTime(dateTime))
                            .content(comment.getCommentBody())
                            .writer(comment.getWriter().getNickname())
                            .build();
                })
                .toList();

        LocalDateTime dateTime = post.getCreatedDate();
        String date = DateUtils.toStringDate(dateTime);
        String time = DateUtils.toStringTime(dateTime);

        return PostResponse.builder()
                .id(post.getId())
                .date(date)
                .time(time)
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getWriter().getNickname())
                .photoPaths(paths)
                .comments(comments)
                .build();
    }


    public static List<PostResponse> of(List<CommunityPost> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }

    public static PostResponse excludePhotosAndCommentsOf(CommunityPost post) {
        LocalDateTime dateTime = post.getCreatedDate();
        String date = DateUtils.toStringDate(dateTime);
        String time = DateUtils.toStringTime(dateTime);

        return PostResponse.builder()
                .id(post.getId())
                .date(date)
                .time(time)
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getWriter().getNickname())
                .build();
    }

    public static List<PostResponse> excludePhotosAndCommentsOf(List<CommunityPost> posts) {
        return posts.stream()
                .map(PostResponse::excludePhotosAndCommentsOf)
                .toList();
    }
}
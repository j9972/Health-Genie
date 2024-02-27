package com.example.healthgenie.boundedContext.community.post.dto;

import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.community.comment.dto.CommentResponse;
import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
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

    public static PostResponse of(Post post) {
        List<String> paths = new ArrayList<>();
        if(post.getPhotos() != null) {
            paths = post.getPhotos().stream()
                    .map(Photo::getPostPhotoPath)
                    .toList();
        }

        List<CommentResponse> comments = post.getComments().stream()
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


    public static List<PostResponse> of(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }

    public static PostResponse excludePhotosAndCommentsOf(Post post) {
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

    public static List<PostResponse> excludePhotosAndCommentsOf(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::excludePhotosAndCommentsOf)
                .toList();
    }
}
package com.example.healthgenie.boundedContext.community.post.dto;


import com.example.healthgenie.boundedContext.community.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PostSliceResponse {

    private List<PostResponse> contents;
    private boolean last;

    public static PostSliceResponse of(Slice<Post> posts) {
        return PostSliceResponse.builder()
                .contents(PostResponse.of(posts.getContent()))
                .last(posts.isLast())
                .build();
    }
}

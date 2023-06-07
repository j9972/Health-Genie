package com.example.healthgenie.dto;

import com.example.healthgenie.entity.CommunityPost;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostIdTitleDto {

    private Long Id;

    private String title;

}

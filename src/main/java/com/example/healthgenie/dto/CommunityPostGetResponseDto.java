package com.example.healthgenie.dto;

import com.example.healthgenie.entity.CommunityComment;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.sql.Blob;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostGetResponseDto {

    private Long id;

    private String title;

    private String body;

    private Long likeCount;

    private Blob pics;

    private List<CommunityComment> commentList;
}

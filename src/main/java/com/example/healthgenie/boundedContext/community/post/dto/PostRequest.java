package com.example.healthgenie.boundedContext.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<MultipartFile> photos;
}
package com.example.healthgenie.boundedContext.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long writerId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<MultipartFile> photos;
}
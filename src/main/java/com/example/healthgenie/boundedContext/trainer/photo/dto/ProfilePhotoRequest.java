package com.example.healthgenie.boundedContext.trainer.photo.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilePhotoRequest {
    @Size(max = 3)
    private List<MultipartFile> photos;
}

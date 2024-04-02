package com.example.healthgenie.boundedContext.trainer.photo.dto;

import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
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
    @Size(max = 5)
    private List<MultipartFile> photos;

    public TrainerPhoto toEntity(TrainerInfo info, String uploadUrl, String originName) {
        return TrainerPhoto.builder()
                .info(info)
                .infoPhotoPath(uploadUrl)
                .name(originName)
                .build();
    }
}

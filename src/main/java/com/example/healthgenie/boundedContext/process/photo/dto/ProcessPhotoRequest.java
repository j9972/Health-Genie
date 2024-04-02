package com.example.healthgenie.boundedContext.process.photo.dto;

import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
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
public class ProcessPhotoRequest {
    @Size(max = 2)
    private List<MultipartFile> photos;

    public ProcessPhoto toEntity(PtProcess process, String uploadUrl, String originName) {
        return ProcessPhoto.builder()
                .process(process)
                .processPhotoPath(uploadUrl)
                .name(originName)
                .build();
    }
}
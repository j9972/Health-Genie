package com.example.healthgenie.boundedContext.process.photo.dto;

import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessPhotoResponse {

    private Long id;
    private String path;
    private String originName;

    public static List<ProcessPhotoResponse> of(List<ProcessPhoto> photos) {
        return photos.stream()
                .map(ProcessPhotoResponse::of)
                .collect(Collectors.toList());
    }

    public static ProcessPhotoResponse of(ProcessPhoto photo) {
        return ProcessPhotoResponse.builder()
                .id(photo.getId())
                .path(photo.getProcessPhotoPath())
                .originName(photo.getName())
                .build();
    }
}

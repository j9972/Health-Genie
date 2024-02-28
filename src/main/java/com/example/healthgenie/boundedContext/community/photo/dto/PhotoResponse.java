package com.example.healthgenie.boundedContext.community.photo.dto;

import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoResponse {

    private Long id;
    private String path;
    private String originName;

    public static List<PhotoResponse> of(List<Photo> photos) {
        return photos.stream()
                .map(PhotoResponse::of)
                .collect(Collectors.toList());
    }

    public static PhotoResponse of(Photo photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .path(photo.getPath())
                .originName(photo.getName())
                .build();
    }
}

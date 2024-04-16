package com.example.healthgenie.boundedContext.trainer.photo.dto;

import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.entity.enums.PurposeOfUsing;
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
public class ProfilePhotoResponse {

    private Long id;
    private String path;
    private String originName;
    private PurposeOfUsing purpose;

    public static List<ProfilePhotoResponse> of(List<TrainerPhoto> photos) {
        return photos.stream()
                .map(ProfilePhotoResponse::of)
                .collect(Collectors.toList());
    }

    public static ProfilePhotoResponse of(TrainerPhoto photo) {
        return ProfilePhotoResponse.builder()
                .id(photo.getId())
                .path(photo.getInfoPhotoPath())
                .originName(photo.getName())
                .purpose(photo.getPurpose())
                .build();
    }
}

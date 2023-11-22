package com.example.healthgenie.domain.ptrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PtProcessPhotoRequestDto {

    private Long processId;
    private String photoPath;
}
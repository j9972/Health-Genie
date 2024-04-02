package com.example.healthgenie.boundedContext.process.process.dto;

import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PtProcessResponseDto {
    private Long id;
    private LocalDate date; // 내가 작성한 날짜가 필요하다.
    private String content;
    private String title;
    private List<String> photoPaths;
    private String userName;
    private String trainerName; // 작성하는 사람

    public static PtProcessResponseDto of(PtProcess process) {
        List<String> photoPaths = process.getPtProcessPhotos().stream()
                .map(ProcessPhoto::getProcessPhotoPath)
                .toList();

        return PtProcessResponseDto.builder()
                .id(process.getId())
                .date(process.getDate())
                .content(process.getContent())
                .title(process.getTitle())
                .photoPaths(photoPaths)
                .userName(process.getMember().getName())
                .trainerName(process.getTrainer().getName())
                .build();
    }

    public static List<PtProcessResponseDto> of(List<PtProcess> process) {
        return process.stream()
                .map(PtProcessResponseDto::of)
                .toList();
    }
}
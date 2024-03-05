package com.example.healthgenie.boundedContext.ptrecord.dto;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class PtProcessResponseDto {
    private Long id;
    private LocalDate date; // 내가 작성한 날짜가 필요하다.
    private String content;
    private String title;
    private List<String> photoPaths;
    private String userNickName;
    private String trainerNickName; // 작성하는 사람
    public static PtProcessResponseDto of(PtProcess process) {
        List<String> photoPaths = process.getPtProcessPhotos().stream()
                .map(PtProcessPhoto::getProcessPhotoPath)
                .toList();

        return PtProcessResponseDto.builder()
                .id(process.getId())
                .date(process.getDate())
                .content(process.getContent())
                .title(process.getTitle())
                .photoPaths(photoPaths)
                .userNickName(process.getMember().getNickname())
                .trainerNickName(process.getTrainer().getNickname())
                .build();
    }

    public static List<PtProcessResponseDto> of(List<PtProcess> process) {
        return process.stream()
                .map(PtProcessResponseDto::of)
                .toList();
    }
}
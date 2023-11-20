package com.example.healthgenie.domain.ptrecord.dto;

import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PtProcessResponseDto {
    private Long id;
    private String date; // 내가 작성한 날짜가 필요하다.
    private String content;
    private String title;
    private String photo;
    private Long userId;
    private Long trainerId; // 작성하는 사람

    public static PtProcessResponseDto of(PtProcess process) {
        return PtProcessResponseDto.builder()
                .id(process.getId())
                .date(process.getDate())
                .content(process.getContent())
                .title(process.getTitle())
                .photo(process.getPhoto())
                .trainerId(process.getTrainer().getId())
                .userId(process.getMember().getId())
                .build();
    }
}
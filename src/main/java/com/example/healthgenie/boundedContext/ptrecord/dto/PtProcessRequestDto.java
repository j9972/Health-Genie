package com.example.healthgenie.boundedContext.ptrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PtProcessRequestDto {
    //private Long id;
    private LocalDate date; // 내가 작성한 날짜가 필요하다.
    private String title;
    private String content;
    private List<MultipartFile> photos;
    private String userNickName;
    private String trainerNickName;
}

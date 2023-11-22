package com.example.healthgenie.domain.ptrecord.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PtProcessRequestDto {
    //private Long id;
    private String date; // 내가 작성한 날짜가 필요하다.
    private String title;
    private String content;
    private List<MultipartFile> photos;
    private Long userId;
    private Long trainerId; // 작성하는 사람
}

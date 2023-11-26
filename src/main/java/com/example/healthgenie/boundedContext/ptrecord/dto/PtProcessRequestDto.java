package com.example.healthgenie.boundedContext.ptrecord.dto;

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
    private String userMail;
    private String trainerMail; // 작성하는 사람
}

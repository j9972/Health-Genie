package com.example.healthgenie.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    //저장
    void saveImage(List<MultipartFile> imagefile);

    //출력
    void displayImage(Long Id);
    //삭제
}

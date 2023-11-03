package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;
import com.example.healthgenie.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface fileService {

    //저장
    List<TrainerPhoto> saveImageList(List<MultipartFile> imagefile, String url, User user);

    String saveImage(MultipartFile imagefile,String url);
    //출력
    void displayImage(Long Id);
    //삭제
    boolean deleteImage(String fileUrlName);
    String modifyImage(String fileUrlName,MultipartFile file,String url);

    List<String> modifyImageList(Long Id,List<MultipartFile> file,String url);
    public File nameingFile(MultipartFile file, String url);
}

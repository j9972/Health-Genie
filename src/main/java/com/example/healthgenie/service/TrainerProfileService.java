package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.dto.TrainerProfileRequestDto;
import com.example.healthgenie.domain.trainer.entity.TrainerInfo;
import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;
import com.example.healthgenie.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrainerProfileService {
    public void fileListToTrainerPhotoList(List<TrainerPhoto> newImageList, List<MultipartFile> fileList, User user);
    public Long profileAddPhotoAndProfile(TrainerProfileRequestDto dto, MultipartFile profile, List<MultipartFile> file, User user);

    public Long profileModifyPhotoAndProfile(TrainerProfileRequestDto Dto, MultipartFile profile, List<MultipartFile> file, TrainerInfo info);

    public TrainerInfo getProfile(Long id);
    public Long profileWrite(TrainerProfileRequestDto profileRequestDto, MultipartFile profile, List<MultipartFile> photo,User user);
}

package com.example.healthgenie.boundedContext.ptrecord.service;

import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PtProcessTransactionSerivce {
    private final S3UploadUtils s3UploadUtils;
    private final PtProcessService processService;
    private final PtProcessPhotoService ptProcessPhotoService;

    public PtProcessResponseDto addPtProcess(PtProcessRequestDto dto, User user) throws IOException {
        List<String> photoPaths = new ArrayList<>();
        PtProcessResponseDto saved = null;
        try {
            // 이미지 S3 저장
            if (existsFile(dto)) {
                photoPaths = s3UploadUtils.upload(dto.getPhotos(), "process-photos");
            }

            // CommunityPost 엔티티 저장
            saved = processService.addPtProcess(dto, user);

            // CommunityPostPhoto 엔티티 저장
            if (existsFile(dto)) {
                ptProcessPhotoService.saveAll(saved.getId(), photoPaths);
            }
        } catch (Exception e) {
            for(String fileUrl : photoPaths) {
                s3UploadUtils.deleteS3Object("process-photos", fileUrl);
            }
            throw e;
        }

        return PtProcessResponseDto.builder()
                .id(saved.getId())
                .date(saved.getDate())
                .content(saved.getContent())
                .title(saved.getTitle())
                .photoPaths(photoPaths)
                .userNickName(saved.getUserNickName())
                .trainerNickName(saved.getTrainerNickName())
                .build();
    }

    private boolean existsFile(PtProcessRequestDto dto) {
        long totalFileSize = dto.getPhotos().stream()
                .mapToLong(MultipartFile::getSize)
                .sum();

        return !dto.getPhotos().isEmpty() && totalFileSize > 0;
    }
}

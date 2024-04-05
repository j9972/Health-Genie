package com.example.healthgenie.boundedContext.process.photo.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.process.photo.dto.ProcessPhotoDeleteResponse;
import com.example.healthgenie.boundedContext.process.photo.dto.ProcessPhotoRequest;
import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import com.example.healthgenie.boundedContext.process.photo.repository.ProcessPhotoRepository;
import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
import com.example.healthgenie.boundedContext.process.process.repository.PtProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.ErrorCode.NO_HISTORY;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessPhotoService {

    private final PtProcessRepository processRepository;
    private final ProcessPhotoRepository processPhotoRepository;
    private final S3UploadUtils s3UploadUtils;


    @Transactional
    public List<ProcessPhoto> save(Long processId, Long userId, ProcessPhotoRequest dto) throws IOException {
        List<ProcessPhoto> photos = new ArrayList<>();

        PtProcess process = processRepository.findById(processId)
                .orElseThrow(() -> new CustomException(NO_HISTORY));

        checkPermission(userId, process);

        for (MultipartFile file : dto.getPhotos()) {
            String uploadUrl = s3UploadUtils.upload(file, "process-photos");
            String originName = file.getOriginalFilename();

            ProcessPhoto savedPhoto = processPhotoRepository.save(
                    dto.toEntity(process, uploadUrl, originName));

            photos.add(savedPhoto);
        }

        return photos;
    }

    @Transactional(readOnly = true)
    public ProcessPhoto findById(Long id) {
        return processPhotoRepository.findById(id)
                .orElseThrow(() -> new CustomException(NO_HISTORY));
    }

    @Transactional(readOnly = true)
    public List<ProcessPhoto> findAllByProcessId(Long processId) {
        return processPhotoRepository.findAllByProcessId(processId);
    }

    @Transactional
    public ProcessPhotoDeleteResponse deleteAllByProcessId(Long processId, Long userId) {
        PtProcess process = processRepository.findById(processId)
                .orElseThrow(() -> new CustomException(NO_HISTORY));

        checkPermission(userId, process);

        List<ProcessPhoto> photos = findAllByProcessId(processId);

        for (ProcessPhoto photo : photos) {
            String path = photo.getProcessPhotoPath();
            s3UploadUtils.deleteS3Object("process-photos", path);
        }

        processPhotoRepository.deleteByProcessId(processId);

        // 어느 프로필의 사진인지 프로필을 id를 반환
        return ProcessPhotoDeleteResponse.builder()
                .id(processId)
                .build();
    }

    private static void checkPermission(Long userId, PtProcess process) {
        if (!Objects.equals(userId, process.getTrainer().getId())) {
            throw new CustomException(NO_PERMISSION);
        }
    }
}

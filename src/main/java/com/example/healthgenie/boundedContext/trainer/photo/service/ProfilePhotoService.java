package com.example.healthgenie.boundedContext.trainer.photo.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.trainer.photo.dto.ProfilePhotoRequest;
import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.repository.TrainerProfilePhotoRepository;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.profile.repository.ProfileRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfilePhotoService {

    private final ProfileRepository profileRepository;
    private final TrainerProfilePhotoRepository trainerProfilePhotoRepository;
    private final S3UploadUtils s3UploadUtils;


    @Transactional
    public List<TrainerPhoto> save(Long profileId, Long userId, ProfilePhotoRequest dto) throws IOException {
        List<TrainerPhoto> photos = new ArrayList<>();

        TrainerInfo profile = profileRepository.findById(profileId)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);

        if (!Objects.equals(userId, profile.getMember().getId())) {
            throw CustomException.NO_PERMISSION;
        }

        for (MultipartFile file : dto.getPhotos()) {
            String uploadUrl = s3UploadUtils.upload(file, "trainer-profile-photos");
            String originName = file.getOriginalFilename();

            TrainerPhoto savedPhoto = trainerProfilePhotoRepository.save(
                    TrainerPhoto.builder()
                            .infoPhotoPath(uploadUrl)
                            .info(profile)
                            .name(originName)
                            .build()
            );

            photos.add(savedPhoto);
        }

        return photos;
    }

    @Transactional
    public List<TrainerPhoto> saveAll(Long profileId, List<String> photoPaths) {
        TrainerInfo profile = profileRepository.findById(profileId)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);

        List<TrainerPhoto> photos = photoPaths.stream()
                .map(path -> TrainerPhoto.builder()
                        .infoPhotoPath(path)
                        .info(profile)
                        .build())
                .toList();

        // 객체 그래프 탐색용
        for (TrainerPhoto photo : photos) {
            profile.addPhoto(photo);
        }

        return trainerProfilePhotoRepository.saveAll(photos);
    }

    @Transactional
    public List<TrainerPhoto> updateAll(Long profileId, List<String> photoPaths) {
        TrainerInfo info = profileRepository.findById(profileId)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);

        // 객체 그래프 탐색용
        info.removePhotos(info.getTrainerPhotos());

        // 기존 Photo 삭제
        trainerProfilePhotoRepository.deleteAllByInfoId(profileId);

        // 새로운 Photo 저장
        return saveAll(profileId, photoPaths);
    }

    @Transactional(readOnly = true)
    public TrainerPhoto findById(Long id) {
        return trainerProfilePhotoRepository.findById(id)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);
    }

    @Transactional(readOnly = true)
    public List<TrainerPhoto> findAll() {
        return trainerProfilePhotoRepository.findAll();
    }
}

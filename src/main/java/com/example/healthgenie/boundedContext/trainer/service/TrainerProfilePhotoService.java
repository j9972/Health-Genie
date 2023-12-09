package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.TrainerProfileErrorResult;
import com.example.healthgenie.base.exception.TrainerProfileException;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfilePhotoRepository;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainerProfilePhotoService {

    private final TrainerProfileRepository trainerProfileRepository;
    private final TrainerProfilePhotoRepository trainerProfilePhotoRepository;

    @Transactional
    public TrainerPhoto save(Long profileId, String path) {
        TrainerInfo profile = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        TrainerPhoto photo = TrainerPhoto.builder()
                .infoPhotoPath(path)
                .info(profile)
                .build();

        return trainerProfilePhotoRepository.save(photo);
    }

    @Transactional
    public List<TrainerPhoto> saveAll(Long profileId, List<String> photoPaths) {
        TrainerInfo profile = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

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
        TrainerInfo info = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

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
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));
    }

    @Transactional(readOnly = true)
    public List<TrainerPhoto> findAll() {
        return trainerProfilePhotoRepository.findAll();
    }
}

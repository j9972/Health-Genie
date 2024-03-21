package com.example.healthgenie.boundedContext.trainer.service;


import com.example.healthgenie.base.exception.TrainerProfileException;
import com.example.healthgenie.base.utils.FileUploadUtils;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileDeleteResponseDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfilePhotoRepository;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerQueryRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerProfileService {
    private final TrainerProfileRepository trainerProfileRepository;
    private final TrainerQueryRepository trainerQueryRepository;
    private final TrainerProfilePhotoRepository trainerProfilePhotoRepository;

    /*
        관리페이지용 API -> 수정 , 트레이너만 가능
    */
    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto dto, Long profileId, User user,
                                            List<MultipartFile> profileImages) {

        TrainerInfo profile = trainerProfileRepository.findByIdAndMemberId(profileId, user.getId())
                .orElseThrow(() -> TrainerProfileException.PROFILE_EMPTY);

        updateProfileItems(dto, profile, profileImages);

        return ProfileResponseDto.of(profile);
    }

    private void updateProfileItems(ProfileRequestDto dto, TrainerInfo profile,
                                    List<MultipartFile> profileImages) {

        updateEachProfile(dto, profile);
        if (profileImages != null && !profileImages.isEmpty()) {
            List<TrainerPhoto> updatedImages = deleteExistedImagesAndUploadNewImages(profile,
                    profileImages);
            profile.updateProfileImages(updatedImages);
        }
    }

    private void updateEachProfile(ProfileRequestDto dto, TrainerInfo profile) {
        if (dto.hasCost()) {
            profile.updateCost(dto.getCost());
        }
        if (dto.hasCareer()) {
            profile.updateCareer(dto.getCareer());
        }
        if (dto.hasMonth()) {
            profile.updateMonth(dto.getMonth());
        }
        if (dto.hasEndTime()) {
            profile.updateEndTime(dto.getEndTime());
        }
        if (dto.hasStartTime()) {
            profile.updateStartTime(dto.getStartTime());
        }
        if (dto.hasReviewAvg()) {
            profile.updateReviewAvg(dto.getReviewAvg());
        }
        if (dto.hasUniversity()) {
            profile.updateUniversity(dto.getUniversity());
        }
        if (dto.hasIntroduction()) {
            profile.updateIntroduction(dto.getIntroduction());
        }
    }

    /*
        관리페이지용 API -> 조회 , 트레이너 본인만 가능
    */
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long profileId) {
        return ProfileResponseDto.of(trainerProfileRepository.findById(profileId).orElseThrow());
    }

    private TrainerInfo authorizationWriter(Long id, User member) {

        TrainerInfo profile = trainerProfileRepository.findById(id)
                .orElseThrow(() -> TrainerProfileException.PROFILE_EMPTY);

        if (!profile.getMember().getId().equals(member.getId())) {
            log.warn("current user doesn't have permission, member : {}", profile.getMember());
            throw TrainerProfileException.NO_PERMISSION;
        }
        return profile;
    }

    // 홈페이지에서 패킷에서 들어가면 보여줄 API
    @Transactional
    public ProfileResponseDto save(User user, ProfileRequestDto dto, List<MultipartFile> profileImages) {

        TrainerInfo info = trainerProfileRepository.save(dto.toEntity(user));
        if (profileImages != null && !profileImages.isEmpty()) {
            List<TrainerPhoto> uploadedImages = uploadProfileImages(info, profileImages);
            info.getTrainerPhotos().addAll(uploadedImages);
        }

        return ProfileResponseDto.of(info);
    }

    @Transactional(readOnly = true)
    public List<ProfileResponseDto> getAllProfile(Long lastIndex) {
        Long maxId = lastIndex;

        if (maxId == null) {
            maxId = trainerQueryRepository.findMaxId().orElse(0L);
        }

        List<TrainerInfo> profiles = trainerQueryRepository.findAllProfilesSortByLatest(maxId);
        return ProfileResponseDto.of(profiles);
    }

    @Transactional(readOnly = true)
    public Slice<TrainerInfo> findAll(String keyword, Long lastId, Pageable pageable) {
        return trainerQueryRepository.findAll(keyword, lastId, pageable);
    }

    private List<TrainerPhoto> uploadProfileImages(TrainerInfo Profile, List<MultipartFile> ProfileImages) {
        return ProfileImages.stream().map(file -> createProfileImage(Profile, file)).toList();
    }

    private TrainerPhoto createProfileImage(TrainerInfo profile, MultipartFile file) {
        String url;
        try {
            url = FileUploadUtils.saveFileAndGetUrl(file);
        } catch (IOException e) {
            log.error("File upload fail", e);
            throw new IllegalArgumentException();
        }

        log.info("url = {}", url);
        profile.clearProfileImages();

        return trainerProfilePhotoRepository.save(
                TrainerPhoto.builder().infoPhotoPath(url).info(profile).build());
    }

    private List<TrainerPhoto> deleteExistedImagesAndUploadNewImages(TrainerInfo profile,
                                                                     List<MultipartFile> profileImages) {
        deleteExistedImages(profile);
        return uploadProfileImages(profile, profileImages);
    }

    private void deleteExistedImages(TrainerInfo profile) {
        trainerProfilePhotoRepository.deleteByInfoId(profile.getId());
    }

    @Transactional
    public ProfileDeleteResponseDto deleteProfile(Long profileId, User user) {

        TrainerInfo profile = authorizationWriter(profileId, user);
        trainerProfileRepository.deleteById(profile.getId());

        return ProfileDeleteResponseDto.builder()
                .id(profile.getId())
                .build();
    }
}

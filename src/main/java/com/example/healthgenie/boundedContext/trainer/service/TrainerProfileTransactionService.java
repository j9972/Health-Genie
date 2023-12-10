package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.exception.TrainerProfileErrorResult;
import com.example.healthgenie.base.exception.TrainerProfileException;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerProfileTransactionService {
    private final S3UploadUtils s3UploadUtils;
    private final TrainerProfileService trainerProfileService;
    private final TrainerProfilePhotoService trainerProfilePhotoService;
    private final TrainerProfileRepository trainerProfileRepository;
    private final UserRepository userRepository;


    @Transactional
    public ProfileResponseDto save(ProfileRequestDto dto)  throws IOException {
        User trainer = userRepository.findByNickname(dto.getNickname())
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        User currentUser = SecurityUtils.getCurrentUser();

        // 작성자와 trainer이 같다면 저장 가능하다
        if (trainer.getEmail().equals(currentUser.getEmail())) {

            List<String> photoPaths = new ArrayList<>();
            ProfileResponseDto savedProfile = null;
            try {
                // 이미지 S3 저장
                if (existsFile(dto)) {
                    photoPaths = s3UploadUtils.upload(dto.getPhotos(), "post-photos");
                }

                // TrainerInfo 엔티티 저장
                savedProfile = trainerProfileService.save(dto);

                // TrainerPhoto 엔티티 저장
                if (existsFile(dto)) {
                    trainerProfilePhotoService.saveAll(savedProfile.getId(), photoPaths);
                }
            } catch (Exception e) {
                for (String fileUrl : photoPaths) {
                    s3UploadUtils.deleteS3Object("post-photos", fileUrl);
                }
                throw e;
            }

            return ProfileResponseDto.builder()
                    .id(savedProfile.getId())
                    .month(savedProfile.getMonth())
                    .nickname(savedProfile.getNickname())
                    .cost(savedProfile.getCost())
                    .university(savedProfile.getUniversity())
                    .introduction(savedProfile.getIntroduction())
                    .startTime(savedProfile.getStartTime())
                    .endTime(savedProfile.getEndTime())
                    .reviewAvg(savedProfile.getReviewAvg())
                    .career(savedProfile.getCareer())
                    .photoPaths(photoPaths)
                    .build();
        }
        throw new TrainerProfileException(TrainerProfileErrorResult.DIFFERENT_USER);
    }

    private boolean existsFile(ProfileRequestDto dto) {
        long totalFileSize = dto.getPhotos().stream()
                .mapToLong(MultipartFile::getSize)
                .sum();

        return !dto.getPhotos().isEmpty() && totalFileSize > 0;
    }

    @Transactional
    public ProfileResponseDto update(Long profileId, ProfileRequestDto dto) throws IOException {
        TrainerInfo info = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        if(!Objects.equals(info.getMember().getId(), SecurityUtils.getCurrentUserId())) {
            throw new TrainerProfileException(TrainerProfileErrorResult.NO_PERMISSION);
        }

        List<String> photoPaths = new ArrayList<>();
        ProfileResponseDto updatedProfile = null;
        try {
            // 이미지 S3 저장
            if (existsFile(dto)) {
                photoPaths = s3UploadUtils.upload(dto.getPhotos(), "post-photos");
            }

            // CommunityPost 엔티티 저장
            updatedProfile = trainerProfileService.updateProfile(dto,profileId);

            // CommunityPostPhoto 엔티티 저장
            if (existsFile(dto)) {
                trainerProfilePhotoService.updateAll(updatedProfile.getId(), photoPaths);
            }
        } catch (Exception e) {
            for(String fileUrl : photoPaths) {
                s3UploadUtils.deleteS3Object("post-photos", fileUrl);
            }
            throw e;
        }

        return ProfileResponseDto.builder()
                .id(updatedProfile.getId())
                .month(updatedProfile.getMonth())
                .nickname(updatedProfile.getNickname())
                .cost(updatedProfile.getCost())
                .university(updatedProfile.getUniversity())
                .introduction(updatedProfile.getIntroduction())
                .startTime(updatedProfile.getStartTime())
                .endTime(updatedProfile.getEndTime())
                .reviewAvg(updatedProfile.getReviewAvg())
                .career(updatedProfile.getCareer())
                .photoPaths(photoPaths)
                .build();
    }
}

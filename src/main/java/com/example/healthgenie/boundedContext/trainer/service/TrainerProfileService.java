package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerProfileService {
    private final TrainerProfileRepository trainerProfileRepository;

    //
    /*
        관리페이지용 API -> 수정 , 트레이너만 가능
    */
    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto dto, Long profileId, User user) {
        TrainerInfo profile = authorizationWriter(profileId, user);

        updateEachProfile(dto, profile);

        return ProfileResponseDto.of(profile);

    }

    private void updateEachProfile(ProfileRequestDto dto, TrainerInfo profile) {
        if (dto.hasCost()){
            profile.updateCost(dto.getCost());
        }
        if (dto.hasCareer()){
            profile.updateCareer(dto.getCareer());
        }
        if (dto.hasMonth()){
            profile.updateMonth(dto.getMonth());
        }
        if (dto.hasEndTime()){
            profile.updateEndTime(dto.getEndTime());
        }
        if (dto.hasStartTime()){
            profile.updateStartTime(dto.getStartTime());
        }
        if (dto.hasReviewAvg()){
            profile.updateReviewAvg(dto.getReviewAvg());
        }
        if (dto.hasUniversity()){
            profile.updateUniversity(dto.getUniversity());
        }
        if (dto.hasIntroduction()){
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

    // review는 회원만 수정 삭제 가능
    private TrainerInfo authorizationWriter(Long id , User member) {

        TrainerInfo profile = trainerProfileRepository.findById(id)
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        if (!profile.getMember().getId().equals(member.getId())) {
            log.warn("current user doesn't have permission, member : {}", profile.getMember());
            throw new TrainerProfileException(TrainerProfileErrorResult.NO_PERMISSION);
        }
        return profile;
    }

    // 홈페이지에 보여줄 패킷용 API

    // 홈페이지에서 패킷에서 들어가면 보여줄 API
    @Transactional
    public ProfileResponseDto save(ProfileRequestDto dto, User currentUser) {

        TrainerInfo savedInfo = trainerProfileRepository.save(
                TrainerInfo.builder()
                        .introduction(dto.getIntroduction())
                        .career(dto.getCareer())
                        .careerMonth(dto.getMonth())
                        .cost(dto.getCost())
                        .name(dto.getName())
                        .university(dto.getUniversity())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .reviewAvg(dto.getReviewAvg())
                        .name(dto.getName())
                        .member(currentUser)
                        .build()
                );

        return ProfileResponseDto.of(savedInfo);
    }

}

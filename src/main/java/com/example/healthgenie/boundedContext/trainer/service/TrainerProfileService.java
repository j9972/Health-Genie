package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.community.dto.PostRequest;
import com.example.healthgenie.boundedContext.community.dto.PostResponse;
import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.NO_PERMISSION;
import static com.example.healthgenie.base.exception.CommunityPostErrorResult.POST_EMPTY;

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

        if(dto.getIntroduction() != null) {
            profile.updateIntroduction(dto.getIntroduction());
        }
        if(dto.getCost() != 0 || profile.getCost() == 0) {
            profile.updateCost(dto.getCost());
        }
        if(dto.getCareer() != null) {
            profile.updateCareer(dto.getCareer());
        }
        if(dto.getMonth() != 0 || profile.getCareerMonth() == 0) {
            profile.updateMonth(dto.getMonth());
        }
        if(dto.getStartTime() != null) {
            profile.updateStartTime(dto.getStartTime());
        }
        if(dto.getEndTime() != null) {
            profile.updateEndTime(dto.getEndTime());
        }
        if(dto.getUniversity() != null) {
            profile.updateUniversity(dto.getUniversity());
        }
        if(dto.getReviewAvg() != 0 || profile.getReviewAvg() == 0 ) {
            profile.updateReviewAvg(dto.getReviewAvg());
        }


        return ProfileResponseDto.of(profile);

    }

    /*
        관리페이지용 API -> 조회 , 트레이너 본인만 가능
    */
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long profileId) {
        TrainerInfo profile = trainerProfileRepository.findById(profileId).orElseThrow(
                () -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        return ProfileResponseDto.of(profile);

    }

    // review는 회원만 수정 삭제 가능
    public TrainerInfo authorizationWriter(Long id , User member) {

        TrainerInfo profile = trainerProfileRepository.findById(id)
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        if (!profile.getMember().getId().equals(member.getId())) {
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

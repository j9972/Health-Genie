package com.example.healthgenie.boundedContext.trainer.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.base.utils.SecurityUtils;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerProfileService {
    private final TrainerProfileRepository trainerProfileRepository;
    private final UserRepository userRepository;

    /*
        트레이너만 가능
     */
    @Transactional
    public ProfileResponseDto writeProfile(ProfileRequestDto dto) {
        User trainer = userRepository.findByNickname(dto.getNickname())
                .orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        User currentUser = SecurityUtils.getCurrentUser();

        // 작성자와 trainer이 같다면 저장 가능하다
        if (trainer.getEmail().equals(currentUser.getEmail())) {

            TrainerInfo profile = TrainerInfo.builder()
                    .introduction(dto.getIntroduction())
                    .cost(dto.getCost())
                    .careerMonth(dto.getMonth())
                    .career(dto.getCareer())
                    .member(currentUser)
                    .build();

            TrainerInfo savedProfile = trainerProfileRepository.save(profile);

            return ProfileResponseDto.ofProfile(savedProfile);
        }

        throw new TrainerProfileException(TrainerProfileErrorResult.DIFFERENT_USER);
    }

    /*
        트레이너만 가능
    */
    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto dto, Long profileId) {
        TrainerInfo profile = authorizationWriter(profileId);

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

        return ProfileResponseDto.ofProfile(profile);

    }

    /*
        관리페이지에서 트레이너만 조회 하는 본인의 프로필
    */
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long profileId) {
        TrainerInfo profile = trainerProfileRepository.findById(profileId).orElseThrow(
                () -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {

            throw new TrainerProfileException(TrainerProfileErrorResult.WRONG_USER);

        } else {
            Optional<User> email = userRepository.findByEmail(authentication.getName());
            User member = userRepository.findById(email.get().getId()).orElseThrow();

            if (profile.getMember().equals(member)) {
                return ProfileResponseDto.ofProfile(profile);
            }
            throw new TrainerProfileException(TrainerProfileErrorResult.WRONG_USER);
        }

    }

    // review는 회원만 수정 삭제 가능
    public TrainerInfo authorizationWriter(Long id) {
        User member = SecurityUtils.getCurrentUser();

        TrainerInfo profile = trainerProfileRepository.findById(id).orElseThrow(() -> new TrainerProfileException(TrainerProfileErrorResult.PROFILE_EMPTY));
        if (!profile.getMember().equals(member)) {
            throw new TrainerProfileException(TrainerProfileErrorResult.USER_EMPTY);
        }
        return profile;
    }


}

package com.example.healthgenie.boundedContext.trainer.profile.service;


import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;
import static com.example.healthgenie.base.exception.ErrorCode.DUPLICATED;
import static com.example.healthgenie.base.exception.ErrorCode.NO_PERMISSION;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileDeleteResponseDto;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileResponseDto;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.profile.repository.ProfileQueryRepository;
import com.example.healthgenie.boundedContext.trainer.profile.repository.ProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileQueryRepository profileQueryRepository;

    /*
        관리페이지용 API -> 수정 , 트레이너만 가능
    */
    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto dto, Long profileId, User user) {

        TrainerInfo profile = profileRepository.findByIdAndMemberId(profileId, user.getId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        updateEachProfile(dto, profile);

        return ProfileResponseDto.of(profile);
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
        return ProfileResponseDto.of(profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND, "trainer 정보")));
    }


    @Transactional(readOnly = true)
    public ProfileResponseDto getOwnProfile(User user) {
        return ProfileResponseDto.of(profileRepository.findByMemberId(user.getId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND, "trainer 정보")));
    }

    private TrainerInfo authorizationWriter(Long id, User member) {

        TrainerInfo profile = profileRepository.findById(id)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        if (!profile.getMember().getId().equals(member.getId())) {
            log.warn("current user doesn't have permission, member : {}", profile.getMember());
            throw new CustomException(NO_PERMISSION);
        }
        return profile;
    }

    // 홈페이지에서 패킷에서 들어가면 보여줄 API
    @Transactional
    public ProfileResponseDto save(User user, ProfileRequestDto dto) {

        // user role 검증
        if (!user.getRole().equals(Role.TRAINER)) {
            throw new CustomException(NO_PERMISSION);
        }
        if (profileRepository.existsByMemberNickname(user.getNickname())) {
            throw new CustomException(DUPLICATED, "trainer info");
        }

        TrainerInfo info = profileRepository.save(dto.toEntity(user));

        return ProfileResponseDto.of(info);
    }

    @Transactional(readOnly = true)
    public List<ProfileResponseDto> getAllProfile(Long lastIndex) {
        Long maxId = lastIndex;

        if (maxId == null) {
            maxId = profileQueryRepository.findMaxId().orElse(0L);
        }

        List<TrainerInfo> profiles = profileQueryRepository.findAllProfilesSortByLatest(maxId);
        return ProfileResponseDto.of(profiles);
    }

    @Transactional(readOnly = true)
    public Slice<TrainerInfo> findAll(String keyword, Long lastId, Pageable pageable) {
        return profileQueryRepository.findAll(keyword, lastId, pageable);
    }

    @Transactional
    public ProfileDeleteResponseDto deleteProfile(Long profileId, User user) {

        TrainerInfo profile = authorizationWriter(profileId, user);
        profileRepository.deleteById(profile.getId());

        return ProfileDeleteResponseDto.builder()
                .id(profile.getId())
                .build();
    }
}

package com.example.healthgenie.boundedContext.ptreview.service;

import com.example.healthgenie.base.exception.MatchingErrorResult;
import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.exception.PtReviewErrorResult;
import com.example.healthgenie.base.exception.PtReviewException;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewQueryRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PtReviewService {

    private final PtReviewRepository ptReviewRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;
    private final PtReviewQueryRepository ptReviewQueryRepository;

    @Transactional
    public PtReviewResponseDto addPtReview(PtReviewRequestDto dto){

        User trainer = userRepository.findByNickname(dto.getTrainerNickName())
                .orElseThrow(() -> new PtReviewException(PtReviewErrorResult.TRAINER_EMPTY));

        matchingRepository.findByMemberNicknameAndTrainerNickname(dto.getUserNickName(), dto.getTrainerNickName())
                .orElseThrow(() -> new MatchingException(MatchingErrorResult.MATCHING_EMPTY));

        PtReview review = ptReviewRepository.findByMemberNicknameAndTrainerNickname(dto.getUserNickName(), dto.getTrainerNickName());


        if (review != null) {
            throw  new PtReviewException(PtReviewErrorResult.DUPLICATED_REVIEW);
        }

        return makePtReview(dto, trainer);
    }

    @Transactional
    public PtReviewResponseDto makePtReview(PtReviewRequestDto dto, User trainer){

        User currentUser = SecurityUtils.getCurrentUser();

        PtReview ptReview = PtReview.builder()
                .content(dto.getContent())
                .reviewScore(dto.getReviewScore())
                .stopReason(dto.getStopReason())
                .member(currentUser)
                .trainer(trainer)
                .build();

        PtReview savedReview = ptReviewRepository.save(ptReview);

        return PtReviewResponseDto.of(savedReview);
    }

    @Transactional(readOnly = true)
    public PtReviewResponseDto getPtReview(Long reviewId) {
        PtReview review = ptReviewRepository.findById(reviewId).orElseThrow(
                () -> new PtReviewException(PtReviewErrorResult.NO_REVIEW_HISTORY));

        /*
            authentication.getPrincipal() == "anonymousUser" -> 현재 사용자가 인증되지 않은 사용자다
        */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        } else {
            Optional<User> email = userRepository.findByEmail(authentication.getName());
            User member = userRepository.findById(email.get().getId()).orElseThrow();

            boolean result = review.getMember().equals(member);

            if (result) {
                return PtReviewResponseDto.of(review);
            }
            throw new  PtReviewException(PtReviewErrorResult.WRONG_USER);
        }
    }

    /*
         특정 trainer review list 조회
         review안에서 trainerId를 조회하는데, review안에는 userId/trainerId가 나뉘어 있어서 필요함
     */
    @Transactional(readOnly = true)
    public Page<PtReviewResponseDto> getAllTrainerReview(int page, int size){
        Long trainerId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtReview> review = ptReviewRepository.findAllByTrainerId(trainerId, pageable);
        return review.map(PtReviewResponseDto::of);
    }

    /*
        본인이 작성한 review list 조회, ]
    */
    @Transactional(readOnly = true)
    public Page<PtReviewResponseDto> getAllReview(int page, int size){
        Long userId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtReview> review = ptReviewRepository.findAllByMemberId(userId, pageable);
        return review.map(PtReviewResponseDto::of);
    }

    @Transactional
    public PtReviewResponseDto updateReview(PtReviewRequestDto dto, Long reviewId){

        PtReview review = authorizationReviewWriter(reviewId);

        if(dto.getContent() != null) {
            review.updateContent(dto.getContent());
        }
        if(dto.getStopReason() != null) {
            review.updateReason(dto.getStopReason());
        }
        if(dto.getReviewScore() != null) {
            review.updateScore(dto.getReviewScore());
        }

        return PtReviewResponseDto.of(review);
    }


    @Transactional
    public void deletePtReview(Long reviewId) {

        PtReview review = authorizationReviewWriter(reviewId);
        ptReviewRepository.deleteById(review.getId());

    }


    // review는 회원만 수정 삭제 가능
    public PtReview authorizationReviewWriter(Long id) {
        User member = SecurityUtils.getCurrentUser();

        PtReview review = ptReviewRepository.findById(id).orElseThrow(() -> new PtReviewException(PtReviewErrorResult.NO_REVIEW_HISTORY));
        if (!review.getMember().equals(member)) {
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        }
        return review;
    }

    public List<PtReviewResponseDto> findAll(String keyword) {
        return PtReviewResponseDto.of(ptReviewQueryRepository.findAll(keyword));
    }

    public List<PtReviewResponseDto> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return PtReviewResponseDto.of(ptReviewQueryRepository.findAllByDate(searchStartDate, searchEndDate));
    }
}

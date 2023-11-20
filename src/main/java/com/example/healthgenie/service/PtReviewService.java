package com.example.healthgenie.service;

import com.example.healthgenie.domain.matching.entity.Matching;
import com.example.healthgenie.domain.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.domain.ptrecord.entity.PtProcess;
import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.domain.ptreview.entity.PtReview;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.*;
import com.example.healthgenie.global.config.SecurityUtil;
import com.example.healthgenie.repository.MatchingRepository;
import com.example.healthgenie.repository.PtReviewRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Slf4j
public class PtReviewService {

    private final PtReviewRepository ptReviewRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;

    @Transactional
    public PtReviewResponseDto addPtReview(PtReviewRequestDto dto, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PtReviewException(PtReviewErrorResult.NO_USER_INFO));

        User trainer = userRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new PtReviewException(PtReviewErrorResult.TRAINER_EMPTY));

        matchingRepository.findByMemberIdAndTrainerId(userId, dto.getTrainerId())
                .orElseThrow(() -> new MatchingException(MatchingErrorResult.MATCHING_EMPTY));

        PtReview reviewHistory = ptReviewRepository.findByMemberIdAndTrainerId(userId, dto.getTrainerId());

        // 해당 리뷰가 있는지 검사 -> 리뷰는 여러개 안됨
        if(reviewHistory != null) {
            throw new PtReviewException(PtReviewErrorResult.DUPLICATED_REVIEW);
        }

        return makePtReview(dto, trainer, user);
    }

    @Transactional
    public PtReviewResponseDto makePtReview(PtReviewRequestDto dto, User trainer, User user){
        PtReview ptReview = PtReview.builder()
                .content(dto.getContent())
                .reviewScore(dto.getReviewScore())
                .stopReason(dto.getStopReason())
                .member(user)
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

        log.info("review : {}",review);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        } else {
            Optional<User> email = userRepository.findByEmail(authentication.getName());
            User member = userRepository.findById(email.get().getId()).orElseThrow();
            log.info("service member : {}", member);
            boolean result = review.getMember().equals(member);

            if (result) {
                return PtReviewResponseDto.of(review);
            } else {
                throw new  PtReviewException(PtReviewErrorResult.WRONG_USER);
            }
        }

    }

    /*
        해당 트레이너에 관한 후기들을 전부 모아보기
     */
    @Transactional(readOnly = true)
    public Page<PtReviewResponseDto> getAllTrainerReview(Long trainerId, int page, int size){
        Page<PtReview> review = ptReviewRepository.findAllByTrainerId(trainerId, PageRequest.of(page, size));
        return review.map(PtReviewResponseDto::of);
    }

    /*
        본인이 작성한 후기들을 전부 모아보기
    */
    @Transactional(readOnly = true)
    public Page<PtReviewResponseDto> getAllMyReview(Long userId, int page, int size){
        Page<PtReview> review = ptReviewRepository.findAllByMemberId(userId, PageRequest.of(page, size));
        return review.map(PtReviewResponseDto::of);
    }

    @Transactional
    public Long editPtReview(PtReviewRequestDto dto, Long reviewId){

        PtReview review = authorizationReviewWriter(reviewId,dto.getUserId());

        if(dto.getContent() != null) {
            review.updateContent(dto.getContent());
        }
        if(dto.getStopReason() != null) {
            review.updateReason(dto.getStopReason());
        }
        if(dto.getReviewScore() != null) {
            review.updateScore(dto.getReviewScore());
        }
        return reviewId;
    }


    @Transactional
    public void deletePtReview(Long reviewId, Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new PtReviewException(PtReviewErrorResult.MEMBER_EMPTY));

        PtReview review = authorizationReviewWriter(reviewId,userId);
        ptReviewRepository.deleteById(review.getId());

    }


    public User isMemberCurrent(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new PtReviewException(PtReviewErrorResult.NO_USER_INFO));
    }

    public PtReview authorizationReviewWriter(Long id, Long userId) {
        User member = isMemberCurrent(userId);

        PtReview review = ptReviewRepository.findById(id).orElseThrow(() -> new PtReviewException(PtReviewErrorResult.NO_REVIEW_HISTORY));
        if (!review.getMember().equals(member)) {
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        }
        return review;
    }

    @Transactional(readOnly = true)
    public Long findById(Long reviewId) {
        Optional<PtReview> review = ptReviewRepository.findById(reviewId);
        log.info("review : {}", review);
        if (review.isPresent()) {
            return review.get().getMember().getId();
        }
        throw new PtReviewException(PtReviewErrorResult.NO_REVIEW_HISTORY);
    }
}

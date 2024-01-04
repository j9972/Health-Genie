package com.example.healthgenie.boundedContext.ptreview.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingQueryRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewQueryRepository;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
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
    private final MatchingUserRepository matchingUserRepository;
    private final PtReviewQueryRepository ptReviewQueryRepository;

    @Transactional
    public PtReviewResponseDto addPtReview(PtReviewRequestDto dto, User user){

        User trainer = userRepository.findByNickname(dto.getTrainerNickName())
                .orElseThrow(() -> new PtReviewException(PtReviewErrorResult.TRAINER_EMPTY));

        Optional<MatchingUser> userMatching = matchingUserRepository.findByUserId(user.getId());
        List<MatchingUser> trainerMatchings = matchingUserRepository.findAllByUserId(trainer.getId());

        if(userMatching.isEmpty()) {
            log.warn("user의 matching 기록이 없습니다");
            throw new MatchingException(MatchingErrorResult.MATCHING_EMPTY);
        }

        PtReview review = ptReviewRepository.findByMemberNicknameAndTrainerNickname(dto.getUserNickName(), dto.getTrainerNickName());

        if (review != null) {
            log.warn("duplicate review : {}", review);
            throw  new PtReviewException(PtReviewErrorResult.DUPLICATED_REVIEW);
        }

        for(MatchingUser match : trainerMatchings) {
            // matching User안에 있는 값들중 matching id값이 같은 경우
            if(match.getMatching().getId().equals(userMatching.get().getMatching().getId())) {

                Optional<Matching> matching = matchingRepository.findById(match.getMatching().getId());

                // trainer와 user사이의 매칭이 있을때 일지 작성 가능
                log.info("해당하는 매칭이 있음 matching : {}", matching);

                // 작성 날짜가 매칭날짜보다 뒤에 있어야 한다
                if(LocalDate.now().isAfter(matching.get().getDate())) {
                    return makePtReview(dto, trainer, user);
                }

                log.warn("일지 작성 날짜가 매칭날짜보다 뒤에 있어야 하는데 그렇지 못함");
                throw new PtReviewException(PtReviewErrorResult.WRONG_DATE);

            }
        }

        log.warn("해당하는 매칭이 없음");
        throw new MatchingException(MatchingErrorResult.MATCHING_EMPTY);
    }

    @Transactional
    public PtReviewResponseDto makePtReview(PtReviewRequestDto dto, User trainer, User currentUser){

        if (!currentUser.getRole().equals(Role.USER)) {
            log.warn("make reivew principal currentUser : {}", currentUser);
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER_ROLE);
        }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            log.warn("no validate user authentication: {}", authentication);
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        } else {
            return PtReviewResponseDto.of(review);
        }
    }

    /*
         특정 trainer review list 조회
         review안에서 trainerId를 조회하는데, review안에는 userId/trainerId가 나뉘어 있어서 필요함

    */
    @Transactional(readOnly = true)
    public Page<PtReviewResponseDto> getAllTrainerReview(Long trainerId, int page, int size){

        Optional<User> user = userRepository.findById(trainerId);
        if (!user.get().getRole().equals(Role.TRAINER)) {
            log.warn("wrong user role : {} ( is not trainer )", user.get().getRole());
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER_ROLE);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtReview> review = ptReviewRepository.findAllByTrainerId(trainerId, pageable);
        return review.map(PtReviewResponseDto::of);
    }

    /*
        본인이 작성한 review list 조회, ]
    */
    @Transactional(readOnly = true)
    public Page<PtReviewResponseDto> getAllReview(Long userId, int page, int size, User currentUser){

        Optional<User> user = userRepository.findById(userId);

        // 본인만 본인의 후기모음을 볼 수 있다
        if (!user.get().getId().equals(currentUser.getId())) {
            log.warn("wrong user role : {} ( is not user )", user.get().getRole());
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        }

        // 트레이너면 후기를 작성할 수 없으니 error
        if(currentUser.getRole().equals(Role.TRAINER)) {
            log.warn("trainer can't write review ( role : {} )", user.get().getRole());
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER_ROLE);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<PtReview> review = ptReviewRepository.findAllByMemberId(currentUser.getId(), pageable);
        return review.map(PtReviewResponseDto::of);
    }

    @Transactional
    public PtReviewResponseDto updateReview(PtReviewRequestDto dto, Long reviewId, User user){

        PtReview review = authorizationReviewWriter(reviewId, user);

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
    public String deletePtReview(Long reviewId, User user) {

        PtReview review = authorizationReviewWriter(reviewId, user);
        ptReviewRepository.deleteById(review.getId());

        return "후기가 삭제 되었습니다.";
    }


    // review는 회원만 수정 삭제 가능
    public PtReview authorizationReviewWriter(Long id, User member) {
        PtReview review = ptReviewRepository.findById(id).orElseThrow(() -> new PtReviewException(PtReviewErrorResult.NO_REVIEW_HISTORY));

        if (!review.getMember().getId().equals(member.getId())) {
            log.warn("this user doesn't have authentication : {}", review.getMember());
            throw new PtReviewException(PtReviewErrorResult.WRONG_USER);
        }
        return review;
    }

    @Transactional(readOnly = true)
    public List<PtReviewResponseDto> findAll(String keyword) {
        return PtReviewResponseDto.of(ptReviewQueryRepository.findAll(keyword));
    }

    @Transactional(readOnly = true)
    public List<PtReviewResponseDto> findAllByDate(LocalDate searchStartDate, LocalDate searchEndDate) {
        return PtReviewResponseDto.of(ptReviewQueryRepository.findAllByDate(searchStartDate, searchEndDate));
    }
}

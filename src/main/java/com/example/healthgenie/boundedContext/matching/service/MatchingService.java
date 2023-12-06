package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.repository.MatchingQueryRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;
import static com.example.healthgenie.base.exception.MatchingErrorResult.*;
import static com.example.healthgenie.boundedContext.matching.entity.MatchingState.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingQueryRepository matchingQueryRepository;
    private final UserRepository userRepository;

    @Transactional
    public MatchingResponse save(MatchingRequest request) {
        User user = userRepository.findByNickname(request.getUserNickname())
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        User trainer = userRepository.findByNickname(request.getTrainerNickname())
                .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        Matching matching = Matching.builder()
                .date(request.getDate())
                .description(request.getDescription())
                .member(user)
                .trainer(trainer)
                .place(request.getPlace())
                .participateState(DEFAULT)
                .acceptState(DEFAULT)
                .build();

        Matching savedMatching = matchingRepository.save(matching);

        return MatchingResponse.of(savedMatching);
    }

    public List<MatchingResponse> findByDateAndNicknames(MatchingRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        List<Matching> matchings =
                matchingQueryRepository.findAllMatchingsForOneDay(request.getDate(), request.getUserNickname(), request.getTrainerNickname());

        return MatchingResponse.of(matchings);
    }

    @Transactional
    public Long participate(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByDateAndMemberId(request.getDate(), currentUserId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        if(!currentUserId.equals(findMatching.getMember().getId())) {
            throw new MatchingException(NO_PERMISSION);
        }

        findMatching.updateParticipateState(PARTICIPATE);

        return findMatching.getId();
    }

    /*
    고민 : participate()와 cancel()을 통합해서 조건문으로 분기해야할까?
          아니면 미래에 요구 사항이 변경으로 로직이 바뀌어서 유지 보수를 위해 이대로 나눠 놔야 할까?
     */
    @Transactional
    public Long cancel(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching =
                matchingQueryRepository.findOne(request.getDate(), request.getUserNickname(), request.getTrainerNickname());

        if(findMatching == null) {
            throw new MatchingException(MATCHING_EMPTY);
        }

        if(!currentUserId.equals(findMatching.getMember().getId())) {
            throw new MatchingException(NO_PERMISSION);
        }

        findMatching.updateParticipateState(CANCEL);

        return findMatching.getId();
    }

    @Transactional
    public Long participateAccept(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByDateAndTrainerId(request.getDate(), currentUserId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        if(!currentUserId.equals(findMatching.getTrainer().getId())) {
            throw new MatchingException(NO_PERMISSION);
        }

        findMatching.updateAcceptState(PARTICIPATE_ACCEPT);

        return findMatching.getId();
    }

    @Transactional
    public Long breakup(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByMemberNicknameAndTrainerNickname(request.getUserNickname(), request.getTrainerNickname())
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        if(!currentUserId.equals(findMatching.getTrainer().getId())) {
            throw new MatchingException(NO_PERMISSION);
        }

        if(!findMatching.getParticipateState().equals(CANCEL)) {
            throw new MatchingException(NOT_CANCELED);
        }

        matchingRepository.delete(findMatching);

        return findMatching.getId();
    }
}

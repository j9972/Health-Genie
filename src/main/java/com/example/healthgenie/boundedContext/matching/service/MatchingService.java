package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;
import static com.example.healthgenie.base.exception.MatchingErrorResult.MATCHING_EMPTY;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchingService {

    private final MatchingRepository matchingRepository;
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
                .isParticipated(false)
                .isAccepted(false)
                .build();

        Matching savedMatching = matchingRepository.save(matching);

        return MatchingResponse.of(savedMatching);
    }

    public List<MatchingResponse> findAllByDate(MatchingRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();

        List<Matching> matchings =  matchingRepository.findAllByDateOrderByDate(request.getDate());

        return MatchingResponse.of(matchings);
    }

    @Transactional
    public String participate(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByDateAndMemberId(request.getDate(), currentUserId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        findMatching.updateParticipated(true);

        return "회원님이 PT에 참석합니다.";
    }

    /*
    고민 : participate()와 cancel()을 통합해서 조건문으로 분기해야할까?
          아니면 미래에 요구 사항이 변경으로 로직이 바뀌어서 유지 보수를 위해 이대로 나눠 놔야 할까?
     */
    @Transactional
    public String cancel(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByDateAndMemberId(request.getDate(), currentUserId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        findMatching.updateParticipated(false);

        return "회원님이 PT를 취소합니다.";
    }

    @Transactional
    public String accept(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByDateAndTrainerId(request.getDate(), currentUserId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        findMatching.updateAccepted(true);

        return "트레이너님이 PT 참석을 승인합니다.";
    }

    /*
    고민 : accept()와 reject()을 통합해서 조건문으로 분기해야할까?
          아니면 미래에 요구 사항이 변경으로 로직이 바뀌어서 유지 보수를 위해 이대로 나눠 놔야 할까?
    */
    @Transactional
    public String reject(MatchingRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Matching findMatching = matchingRepository.findByDateAndTrainerId(request.getDate(), currentUserId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        findMatching.updateAccepted(false);

        return "트레이너님이 PT 취소를 승인합니다.";
    }
}

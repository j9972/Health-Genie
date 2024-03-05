package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.utils.CustomValidator.CommonValidator;
import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingResponse;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingQueryRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.healthgenie.base.exception.MatchingErrorResult.*;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;
import static com.example.healthgenie.boundedContext.matching.entity.MatchingState.*;
import static com.example.healthgenie.boundedContext.user.entity.Role.TRAINER;
import static com.example.healthgenie.boundedContext.user.entity.Role.USER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final MatchingQueryRepository matchingQueryRepository;
    private final UserRepository userRepository;

    @Transactional
    public MatchingResponse save(Long userId, Long trainerId, String date, String time, String place, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        validateSave(user, trainer);

        Matching matching = Matching.builder()
                .date(DateUtils.toLocalDate(date))
                .time(DateUtils.toLocalTime(time))
                .description(description)
                .place(place)
                .state(DEFAULT)
                .build();

        MatchingUser matchingUser1 = MatchingUser.builder()
                .matching(matching)
                .user(user)
                .build();

        MatchingUser matchingUser2 = MatchingUser.builder()
                .matching(matching)
                .user(trainer)
                .build();

        matchingUserRepository.save(matchingUser1);
        matchingUserRepository.save(matchingUser2);

        matching.getMatchingUsers().add(matchingUser1);
        matching.getMatchingUsers().add(matchingUser2);

        return MatchingResponse.of(matchingRepository.save(matching));
    }

    public MatchingResponse findOne(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        return MatchingResponse.of(matching);
    }

    public List<MatchingResponse> findAll(User user, MatchingCondition condition) {
        if(!CommonValidator.isValid(condition.getDate())) {
            throw new MatchingException(NOT_VALID_FIELD);
        }

        List<Matching> matchings =
                matchingQueryRepository.findAllByUserIdAndDate(user.getId(), DateUtils.toLocalDate(condition.getDate()));

        return MatchingResponse.of(matchings);
    }

    @Transactional
    public MatchingResponse update(Long matchingId, MatchingState state) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));

        User currentUser = SecurityUtils.getCurrentUser();

        validatePermission(currentUser, state);
        validateUpdate(matching, state);

        matching.updateState(state);

        return MatchingResponse.of(matching);
    }

    private void validateUpdate(Matching matching, MatchingState changeState) {
        MatchingState currentState = matching.getState();

        if(currentState.equals(CANCEL_ACCEPT)) {
            throw new MatchingException(NO_PERMISSION);
        }

        if(currentState.equals(DEFAULT)) {
            if(changeState.equals(PARTICIPATE) || changeState.equals(CANCEL)) {
                return;
            }
        }
        else if(currentState.equals(PARTICIPATE) && changeState.equals(PARTICIPATE_ACCEPT)) {
            return;
        }
        else if(currentState.equals(CANCEL) && changeState.equals(CANCEL_ACCEPT)) {
            return;
        }

        throw new MatchingException(NO_PERMISSION);
    }

    private void validateSave(User user, User trainer) {
        if(!user.getRole().equals(USER) || !trainer.getRole().equals(TRAINER)) {
            throw new MatchingException(NO_PERMISSION);
        }
    }

    private void validatePermission(User user, MatchingState state) {
        switch (state) {
            case PARTICIPATE, CANCEL -> {
                if(!user.getRole().equals(USER)) {
                    throw new MatchingException(NO_PERMISSION);
                }
            }
            case PARTICIPATE_ACCEPT, CANCEL_ACCEPT -> {
                if(!user.getRole().equals(TRAINER)) {
                    throw new MatchingException(NO_PERMISSION);
                }
            }
            default -> throw new MatchingException(NOT_VALID_FIELD);
        }
    }
}

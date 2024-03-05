package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.base.exception.MatchingException;
import com.example.healthgenie.boundedContext.matching.dto.MatchingCondition;
import com.example.healthgenie.boundedContext.matching.dto.MatchingRequest;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingState;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.repository.MatchingQueryRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.healthgenie.base.exception.MatchingErrorResult.*;
import static com.example.healthgenie.boundedContext.matching.entity.MatchingState.*;
import static com.example.healthgenie.boundedContext.user.entity.Role.TRAINER;
import static com.example.healthgenie.boundedContext.user.entity.Role.USER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingQueryRepository matchingQueryRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final UserService userService;

    @Transactional
    public Matching save(User trainer, MatchingRequest request) {
        User user = userService.findById(request.getUserId());

        validateSave(user, trainer);

        Matching matching = matchingRepository.save(
                Matching.builder()
                        .date(request.getDate())
                        .description(request.getDescription())
                        .place(request.getPlace())
                        .state(DEFAULT)
                        .build()
        );

        mapMatchingUser(user, trainer, matching);

        return matching;
    }

    public Matching findById(Long matchingId) {
        return matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MATCHING_EMPTY));
    }

    public Matching findById(Long matchingId, User user) {
        Matching matching = findById(matchingId);

        for(MatchingUser mu : matching.getMatchingUsers()) {
            if(Objects.equals(mu.getUser(), user)) {
                return matching;
            }
        }

        throw new MatchingException(NO_PERMISSION);
    }

    public List<Matching> findAll(User user, MatchingCondition condition) {
        return matchingQueryRepository.findAllByUserIdAndDate(user.getId(), condition.getDate());
    }

    @Transactional
    public Matching update(Long matchingId, User user, MatchingState state) {
        Matching matching = findById(matchingId);

        validatePermission(user, state);
        validateUpdate(matching, state);

        matching.updateState(state);

        return matching;
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

    private void mapMatchingUser(User user, User trainer, Matching matching) {
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
    }
}

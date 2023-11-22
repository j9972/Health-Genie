package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.boundedContext.matching.dto.MatchingDto;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final MatchingRepository matchingRepository;

    @Transactional
    public MatchingDto add(MatchingDto dto) {
        Matching matching = Matching.builder()
                .ptDate(dto.getPtDate())
                .ptPlace(dto.getPtPlace())
                .ptAccept(true)
                .price(dto.getPrice())
                .ptExperience(true)
                .member(User.builder().id(dto.getUserId()).build())
                .trainer(User.builder().id(dto.getTrainerId()).build())
                .build();

        Matching savedMatching = matchingRepository.save(matching);

        return MatchingDto.builder()
                .id(savedMatching.getId())
                .ptDate(savedMatching.getPtDate())
                .ptPlace(savedMatching.getPtPlace())
                .ptAccept(savedMatching.isPtAccept())
                .price(savedMatching.getPrice())
                .ptExperience(savedMatching.isPtExperience())
                .userId(savedMatching.getMember().getId())
                .trainerId(savedMatching.getTrainer().getId())
                .build();
    }
}

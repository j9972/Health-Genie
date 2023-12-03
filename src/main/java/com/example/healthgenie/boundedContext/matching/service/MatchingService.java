package com.example.healthgenie.boundedContext.matching.service;

import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final MatchingRepository matchingRepository;
}

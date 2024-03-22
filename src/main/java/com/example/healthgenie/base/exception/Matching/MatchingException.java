package com.example.healthgenie.base.exception.Matching;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchingException extends RuntimeException {
    private final MatchingErrorResult matchingErrorResult;
}

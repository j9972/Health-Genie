package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchingException extends RuntimeException{
    private final MatchingErrorResult matchingErrorResult;
}

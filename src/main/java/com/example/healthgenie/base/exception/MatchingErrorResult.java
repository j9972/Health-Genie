package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchingErrorResult {
    MATCHING_EMPTY(HttpStatus.BAD_REQUEST,"matching is empty Request");


    private final HttpStatus httpStatus;
    private final String message;
}

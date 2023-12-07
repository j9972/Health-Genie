package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchingErrorResult {

    NOT_VALID_FIELD(HttpStatus.BAD_REQUEST,"matching is empty"),
    MATCHING_EMPTY(HttpStatus.BAD_REQUEST,"matching is empty"),
    NO_PERMISSION(HttpStatus.BAD_REQUEST,"don't have permission"),
    NOT_CANCELED(HttpStatus.BAD_REQUEST,"matching is not canceled")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

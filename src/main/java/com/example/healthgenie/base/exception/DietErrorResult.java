package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DietErrorResult {

    TYPE_EMPTY(HttpStatus.BAD_REQUEST, "type is empty")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

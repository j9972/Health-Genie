package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorResult {

    ITEM_EMPTY(HttpStatus.BAD_REQUEST,"No items"),
    VALID_OUT(HttpStatus.INTERNAL_SERVER_ERROR,"verification failed"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"bad request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"unauthorized");

    private final HttpStatus httpStatus;
    private final String message;
}

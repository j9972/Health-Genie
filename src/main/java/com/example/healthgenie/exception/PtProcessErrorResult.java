package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PtProcessErrorResult {
    TRAINER_EMPTY(HttpStatus.BAD_REQUEST,"trainer is empty Request"),
    USER_EMPTY(HttpStatus.BAD_REQUEST,"user is empty Request"),
    RECORD_EMPTY(HttpStatus.BAD_REQUEST,"record is empty Request"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

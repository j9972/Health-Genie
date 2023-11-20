package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PtProcessErrorResult {
    TRAINER_EMPTY(HttpStatus.BAD_REQUEST,"trainer is empty Request"),
    USER_EMPTY(HttpStatus.BAD_REQUEST,"user is empty Request"),
    WRONG_USER(HttpStatus.BAD_REQUEST,"user is wrong Request"),
    RECORD_EMPTY(HttpStatus.BAD_REQUEST,"record is empty Request"),
    NO_USER_INFO(HttpStatus.BAD_REQUEST,"user info is empty Request"),
    NO_PROCESS_HISTORY(HttpStatus.BAD_REQUEST,"feedback is empty Request"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

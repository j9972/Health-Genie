package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserEmailErrorResult {

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "Duplicated email Request"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST,"Email is not valid"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"Auth impossible"),
    BAD_CREDENTIALS(HttpStatus.INTERNAL_SERVER_ERROR,"Bad Credentials")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

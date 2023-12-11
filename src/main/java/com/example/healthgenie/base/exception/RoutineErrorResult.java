package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoutineErrorResult {
    NO_USER_INFO(HttpStatus.BAD_REQUEST,"no user information"),
    NO_HISTORY(HttpStatus.BAD_REQUEST,"no history"),
    DIFFERNET_NICKNAME(HttpStatus.BAD_REQUEST,"wrong user nickname"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

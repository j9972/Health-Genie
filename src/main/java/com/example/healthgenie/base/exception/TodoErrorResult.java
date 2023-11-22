package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum TodoErrorResult {
    TODO_EMPTY(HttpStatus.BAD_REQUEST,"todo is empty Request"),
    NO_USER_INFO(HttpStatus.BAD_REQUEST,"user info is empty Request"),
    NO_TODO_INFO(HttpStatus.BAD_REQUEST,"todo info is empty Request"),
    WRONG_USER(HttpStatus.BAD_REQUEST,"wrong user Request"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
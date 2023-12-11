package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TrainerProfileErrorResult {

    PROFILE_EMPTY(HttpStatus.BAD_REQUEST,"profile is empty Request"),
    DIFFERENT_USER(HttpStatus.BAD_REQUEST,"current user and trainer are different"),
    USER_EMPTY(HttpStatus.BAD_REQUEST,"user is empty"),
    WRONG_USER(HttpStatus.BAD_REQUEST,"user is wrong"),
    NO_PERMISSION(HttpStatus.BAD_REQUEST,"user doesn't have permission"),
    PROFILE_EXIST(HttpStatus.BAD_REQUEST,"trainer profile exist"),
    USER_IS_NOT_TRAINER(HttpStatus.BAD_REQUEST,"user is not trainer"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception");


    private final HttpStatus httpStatus;
    private final String message;
}

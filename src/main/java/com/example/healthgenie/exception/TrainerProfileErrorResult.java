package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TrainerProfileErrorResult {

    PROFILE_EMPTY(HttpStatus.BAD_REQUEST,"profile is empty Request"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception");


    private final HttpStatus httpStatus;
    private final String message;
}

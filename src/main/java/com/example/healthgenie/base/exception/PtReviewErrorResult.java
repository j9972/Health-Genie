package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PtReviewErrorResult {

    DUPLICATED_REVIEW(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    NO_REVIEW_HISTORY(HttpStatus.BAD_REQUEST, "No Reivew History For Edit"),
    WRONG_USER(HttpStatus.BAD_REQUEST, "Wrong User"),
    TRAINER_EMPTY(HttpStatus.BAD_REQUEST,"trainer is empty"),
    MEMBER_EMPTY(HttpStatus.BAD_REQUEST,"member is empty"),
    NO_USER_INFO(HttpStatus.BAD_REQUEST,"user info is empty"),
    WRONG_USER_ROLE(HttpStatus.BAD_REQUEST,"user role is trainer not user"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

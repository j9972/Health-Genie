package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorResult {

    ALREADY_EXISTS_ROLE(HttpStatus.BAD_REQUEST, "already role exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "not found user"),
    ITEM_EMPTY(HttpStatus.BAD_REQUEST,"No items"),
    VALID_OUT(HttpStatus.INTERNAL_SERVER_ERROR,"verification failed"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"bad request"),
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST,"unable to send email"),
    MEMBER_EXISTS(HttpStatus.BAD_REQUEST,"member already exists"),
    NO_SUCH_ALGORITHM(HttpStatus.BAD_REQUEST,"no such algorithm"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"unauthorized");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorResult {

    NO_PERMISSION(HttpStatus.BAD_REQUEST, "don't have any permissions"),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "duplicated nickname"),
    NOT_VALID_FIELD(HttpStatus.BAD_REQUEST, "field is not valid"),
    ALREADY_EXISTS_ROLE(HttpStatus.BAD_REQUEST, "already role exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user is not found!!"),
    ALREADY_SIGN_UP(HttpStatus.NOT_FOUND, "already sign up")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

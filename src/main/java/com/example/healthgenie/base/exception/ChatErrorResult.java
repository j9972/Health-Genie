package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorResult {

    NO_PERMISSION(HttpStatus.BAD_REQUEST, "don't have any permissions"),
    SELF_CHAT(HttpStatus.BAD_REQUEST, "don't create self chat room"),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "not found room");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.example.healthgenie.base.exception.Like;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorResult {

    NO_PERMISSION(HttpStatus.BAD_REQUEST, "don't have permission"),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "like is not found"),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "already liked");

    private final HttpStatus httpStatus;
    private final String message;
}

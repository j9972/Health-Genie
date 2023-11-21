package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum CommunityPostErrorResult {

    PHOTO_EMPTY(HttpStatus.BAD_REQUEST, "photo is empty"),
    NO_PERMISSION(HttpStatus.BAD_REQUEST, "don't have permission"),
    POST_EMPTY(HttpStatus.BAD_REQUEST,"post is empty Request"),
    UNkNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"unknown_Exception"),
    PAGE_EMPTY(HttpStatus.BAD_REQUEST,"post is empty Request");

    private final HttpStatus httpStatus;
    private final String message;
}

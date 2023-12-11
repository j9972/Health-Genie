package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommunityCommentErrorResult {

    NO_PERMISSION(HttpStatus.BAD_REQUEST, "don't have permission"),
    COMMENT_EMPTY(HttpStatus.BAD_REQUEST, "comment is empty"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "unknown_Exception");

    private final HttpStatus httpStatus;
    private final String message;
}

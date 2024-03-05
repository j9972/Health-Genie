package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikeException extends RuntimeException {
    private final LikeErrorResult likeErrorResult;
}

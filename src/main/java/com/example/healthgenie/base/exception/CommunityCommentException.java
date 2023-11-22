package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityCommentException extends RuntimeException{
    private final CommunityCommentErrorResult communityCommentErrorResult;
}

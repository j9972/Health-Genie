package com.example.healthgenie.base.exception.Post;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityPostException extends RuntimeException {
    private final CommunityPostErrorResult communityPostErrorResult;
}

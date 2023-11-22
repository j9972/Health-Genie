package com.example.healthgenie.base.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityPostException extends RuntimeException{
     private final CommunityPostErrorResult communityPostErrorResult;
}

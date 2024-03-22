package com.example.healthgenie.base.exception.Chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatException extends RuntimeException {
    private final ChatErrorResult chatErrorResult;
}

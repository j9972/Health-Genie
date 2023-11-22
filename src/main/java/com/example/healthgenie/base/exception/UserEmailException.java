package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserEmailException extends RuntimeException{

    private final UserEmailErrorResult userEmailErrorResult;
}
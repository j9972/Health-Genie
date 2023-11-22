package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PtProcessException extends RuntimeException{

    private final PtProcessErrorResult ptProcessErrorResult;
}
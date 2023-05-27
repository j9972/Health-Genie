package com.example.healthgenie.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PtReviewException extends RuntimeException{

    private final PtReviewErrorResult ptReviewErrorResult;
}

package com.example.healthgenie.base.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String msg) {
        super(errorCode.getMessage() + " : " + msg);
        this.errorCode = errorCode;
    }
}

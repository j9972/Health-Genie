package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 4xx
    DATA_NOT_FOUND(BAD_REQUEST, "해당 데이터를 찾을 수 없습니다."),
    NO_HISTORY(BAD_REQUEST, "기록이 없습니다."),
    DUPLICATED(BAD_REQUEST, "중복된 데이터입니다."),
    NOT_VALID(BAD_REQUEST, "유효하지 않은 값입니다."),
    NO_PERMISSION(BAD_REQUEST, "권한이 없습니다."),
    JWT_ERROR(UNAUTHORIZED, "JWT 에러입니다."),

    // 5xx
    UNKNOWN_EXCEPTION(INTERNAL_SERVER_ERROR, "알 수 없는 예외입니다. 관리자에게 문의해주세요.");

    private final HttpStatus status;
    private final String message;
}

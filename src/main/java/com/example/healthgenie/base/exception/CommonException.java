package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CommonException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static final CommonException ALREADY_EXISTS_ROLE = new CommonException(HttpStatus.BAD_REQUEST,
            "already role exists");
    public static final CommonException ITEM_EMPTY = new CommonException(HttpStatus.BAD_REQUEST,
            "No items");
    public static final CommonException USER_NOT_FOUND = new CommonException(HttpStatus.NOT_FOUND,
            "not found user");
    public static final CommonException WRONG_VALIDATE_EMAIL = new CommonException(HttpStatus.BAD_REQUEST,
            "Wrong email validate");
    public static final CommonException VALID_OUT = new CommonException(HttpStatus.INTERNAL_SERVER_ERROR,
            "verification failed");
    public static final CommonException BAD_REQUEST = new CommonException(HttpStatus.BAD_REQUEST,
            "bad request");
    public static final CommonException UNABLE_TO_SEND_EMAIL = new CommonException(HttpStatus.BAD_REQUEST,
            "unable to send email");
    public static final CommonException WRONG_DOMAIN = new CommonException(HttpStatus.BAD_REQUEST,
            "domain is wrong for sending");
    public static final CommonException MEMBER_EXISTS = new CommonException(HttpStatus.BAD_REQUEST,
            "member already exists");
    public static final CommonException NO_SUCH_ALGORITHM = new CommonException(HttpStatus.BAD_REQUEST,
            "no such algorithm");
    public static final CommonException UNAUTHORIZED = new CommonException(HttpStatus.UNAUTHORIZED,
            "unauthorized");

}

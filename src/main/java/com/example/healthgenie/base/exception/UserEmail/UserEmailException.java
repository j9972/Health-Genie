package com.example.healthgenie.base.exception.UserEmail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class UserEmailException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static final UserEmailException DUPLICATED_EMAIL = new UserEmailException(HttpStatus.BAD_REQUEST,
            "Duplicated email Request");
    public static final UserEmailException INVALID_EMAIL = new UserEmailException(HttpStatus.BAD_REQUEST,
            "Email is not valid");
    public static final UserEmailException UNkNOWN_EXCEPTION = new UserEmailException(HttpStatus.INTERNAL_SERVER_ERROR,
            "unknown_Exception");
    public static final UserEmailException BAD_CREDENTIALS = new UserEmailException(HttpStatus.BAD_REQUEST,
            "Bad Credentialsm");
    public static final UserEmailException UNAUTHORIZED = new UserEmailException(HttpStatus.UNAUTHORIZED,
            "Auth impossible");
}
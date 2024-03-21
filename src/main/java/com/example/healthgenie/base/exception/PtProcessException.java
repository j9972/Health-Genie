package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class PtProcessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static final PtProcessException TRAINER_EMPTY = new PtProcessException(HttpStatus.BAD_REQUEST,
            "trainer is empty Request");
    public static final PtProcessException USER_EMPTY = new PtProcessException(HttpStatus.BAD_REQUEST,
            "user is empty Request");
    public static final PtProcessException WRONG_USER = new PtProcessException(HttpStatus.BAD_REQUEST,
            "user is wrong Request");
    public static final PtProcessException WRONG_DATE = new PtProcessException(HttpStatus.BAD_REQUEST,
            "process have to write after matching date");
    public static final PtProcessException RECORD_EMPTY = new PtProcessException(HttpStatus.BAD_REQUEST,
            "record is empty Request");
    public static final PtProcessException WRONG_USER_ROLE = new PtProcessException(HttpStatus.BAD_REQUEST,
            "role is trainer not user");
    public static final PtProcessException NO_USER_INFO = new PtProcessException(HttpStatus.BAD_REQUEST,
            "user info is empty Request");
    public static final PtProcessException NO_PROCESS_HISTORY = new PtProcessException(HttpStatus.BAD_REQUEST,
            "feedback is empty Request");
    public static final PtProcessException UNkNOWN_EXCEPTION = new PtProcessException(HttpStatus.INTERNAL_SERVER_ERROR,
            "unknown_Exception");
}
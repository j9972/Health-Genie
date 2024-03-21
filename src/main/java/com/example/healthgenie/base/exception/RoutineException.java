package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class RoutineException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public static final RoutineException NO_USER_INFO = new RoutineException(HttpStatus.BAD_REQUEST,
            "no user information");
    public static final RoutineException NO_HISTORY = new RoutineException(HttpStatus.BAD_REQUEST,
            "no history");
    public static final RoutineException DUPLICATE_DAY = new RoutineException(HttpStatus.BAD_REQUEST,
            "duplicate day");
    public static final RoutineException DIFFERENT_NICKNAME = new RoutineException(HttpStatus.BAD_REQUEST,
            "wrong user nickname");
    public static final RoutineException UNkNOWN_EXCEPTION = new RoutineException(HttpStatus.INTERNAL_SERVER_ERROR,
            "unknown_Exception");
}

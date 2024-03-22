package com.example.healthgenie.base.exception.Todo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class TodoException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static final TodoException TODO_EMPTY = new TodoException(HttpStatus.BAD_REQUEST,
            "todo is empty Request");
    public static final TodoException NO_USER_INFO = new TodoException(HttpStatus.BAD_REQUEST,
            "user info is empty Request");
    public static final TodoException NO_TODO_INFO = new TodoException(HttpStatus.BAD_REQUEST,
            "todo info is empty Request");
    public static final TodoException WRONG_USER = new TodoException(HttpStatus.BAD_REQUEST,
            "wrong user Request");
    public static final TodoException UNkNOWN_EXCEPTION = new TodoException(HttpStatus.INTERNAL_SERVER_ERROR,
            "unknown_Exception");
}
package com.example.healthgenie.base.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class TrainerProfileException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static final TrainerProfileException PROFILE_EMPTY = new TrainerProfileException(HttpStatus.BAD_REQUEST,
            "profile is empty Request");
    public static final TrainerProfileException DIFFERENT_USER = new TrainerProfileException(HttpStatus.BAD_REQUEST,
            "current user and trainer are different");
    public static final TrainerProfileException USER_EMPTY = new TrainerProfileException(HttpStatus.BAD_REQUEST,
            "user is empty");
    public static final TrainerProfileException WRONG_USER = new TrainerProfileException(
            HttpStatus.BAD_REQUEST,
            "user is wrong");
    public static final TrainerProfileException NO_PERMISSION = new TrainerProfileException(HttpStatus.BAD_REQUEST,
            "user doesn't have permission");
    public static final TrainerProfileException PROFILE_EXIST = new TrainerProfileException(HttpStatus.BAD_REQUEST,
            "trainer profile exist");
    public static final TrainerProfileException USER_IS_NOT_TRAINER = new TrainerProfileException(
            HttpStatus.BAD_REQUEST,
            "user is not trainer");
    public static final TrainerProfileException UNkNOWN_EXCEPTION = new TrainerProfileException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "unknown_Exception");
}

package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class PtReviewException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public static final PtReviewException DUPLICATED_REVIEW = new PtReviewException(HttpStatus.BAD_REQUEST,
            "Duplicated Membership Register Request");
    public static final PtReviewException NO_REVIEW_HISTORY = new PtReviewException(HttpStatus.BAD_REQUEST,
            "No Reivew History For Edit");
    public static final PtReviewException WRONG_USER = new PtReviewException(HttpStatus.BAD_REQUEST,
            "Wrong User");
    public static final PtReviewException WRONG_DATE = new PtReviewException(HttpStatus.BAD_REQUEST,
            "review write after mathching date");
    public static final PtReviewException TRAINER_EMPTY = new PtReviewException(HttpStatus.BAD_REQUEST,
            "trainer is empty");
    public static final PtReviewException MEMBER_EMPTY = new PtReviewException(HttpStatus.BAD_REQUEST,
            "member is empty");
    public static final PtReviewException NO_USER_INFO = new PtReviewException(HttpStatus.BAD_REQUEST,
            "user info is empty");
    public static final PtReviewException WRONG_USER_ROLE = new PtReviewException(HttpStatus.BAD_REQUEST,
            "user role is trainer not user");
    public static final PtReviewException UNkNOWN_EXCEPTION = new PtReviewException(HttpStatus.INTERNAL_SERVER_ERROR,
            "unknown_Exception");
}

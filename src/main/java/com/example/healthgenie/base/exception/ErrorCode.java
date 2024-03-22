package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 4xx
    // PtProcessError
    TRAINER_EMPTY(BAD_REQUEST, "trainer is empty Request"),
    USER_EMPTY(BAD_REQUEST, "user is empty Request"),
    WRONG_USER(BAD_REQUEST, "user is wrong Request"),
    WRONG_DATE(BAD_REQUEST, "process have to write after matching date"),
    RECORD_EMPTY(BAD_REQUEST, "record is empty Request"),
    WRONG_USER_ROLE(BAD_REQUEST, "role is trainer not user"),
    NO_USER_INFO(BAD_REQUEST, "user info is empty Request"),
    NO_PROCESS_HISTORY(BAD_REQUEST, "feedback is empty Request"),

    // ChatError
    NO_PERMISSION(BAD_REQUEST, "don't have any permissions"),
    SELF_CHAT(BAD_REQUEST, "don't create self chat room"),
    ROOM_NOT_FOUND(BAD_REQUEST, "not found room"),

    // CommentError
    COMMENT_EMPTY(BAD_REQUEST, "comment is empty"),

    // CommonError
    ALREADY_EXISTS_ROLE(BAD_REQUEST, "already role exists"),
    ITEM_EMPTY(BAD_REQUEST, "No items"),
    USER_NOT_FOUND(NOT_FOUND, "not found user"),
    WRONG_VALIDATE_EMAIL(BAD_REQUEST, "Wrong email validate"),
    VALID_OUT(INTERNAL_SERVER_ERROR, "verification failed"),
    UNABLE_TO_SEND_EMAIL(BAD_REQUEST, "unable to send email"),
    WRONG_DOMAIN(BAD_REQUEST, "domain is wrong for sending"),
    MEMBER_EXISTS(BAD_REQUEST, "member already exists"),
    NO_SUCH_ALGORITHM(BAD_REQUEST, "no such algorithm"),

    // DietError
    TYPE_EMPTY(BAD_REQUEST, "type is empty"),

    // LikeError
    LIKE_NOT_FOUND(BAD_REQUEST, "like is not found"),
    ALREADY_LIKED(BAD_REQUEST, "already liked"),

    // MatchingError
    NOT_VALID_FIELD(BAD_REQUEST, "matching is empty"),
    MATCHING_EMPTY(BAD_REQUEST, "matching is empty"),
    TOO_EARLY_TO_WRITE_FEEDBACK(BAD_REQUEST, "writing process is early than matching date"),
    NOT_CANCELED(BAD_REQUEST, "matching is not canceled"),

    // PostError
    PHOTO_EMPTY(BAD_REQUEST, "photo is empty"),
    POST_EMPTY(BAD_REQUEST, "post is empty"),
    PAGE_EMPTY(BAD_REQUEST, "post is empty Request"),

    // PtReviewError
    PtReviewException(BAD_REQUEST, "Duplicated Membership Register Request"),
    NO_REVIEW_HISTORY(BAD_REQUEST, "No Reivew History For Edit"),
    MEMBER_EMPTY(BAD_REQUEST, "member is empty"),

    // RoutineError
    NO_HISTORY(BAD_REQUEST, "no history"),
    DUPLICATE_DAY(BAD_REQUEST, "duplicate day"),
    DIFFERENT_NICKNAME(BAD_REQUEST, "wrong user nickname"),

    // TodoError
    TODO_EMPTY(BAD_REQUEST, "todo is empty Request"),
    NO_TODO_INFO(BAD_REQUEST, "todo info is empty Request"),

    // TrainerProfileError
    PROFILE_EMPTY(BAD_REQUEST, "profile is empty Request"),
    DIFFERENT_USER(BAD_REQUEST, "current user and trainer are different"),
    PROFILE_EXIST(BAD_REQUEST, "trainer profile exist"),
    USER_IS_NOT_TRAINER(BAD_REQUEST, "user is not trainer"),

    // JwtError
    JWT_EMPTY(UNAUTHORIZED, "you need to request with JWT"),
    WRONG_SIGNATURE(UNAUTHORIZED, "wrong signature"),
    EXPIRED_TOKEN(UNAUTHORIZED, "expired token"),
    UNSUPPORTED(UNAUTHORIZED, "unsupported token"),
    WRONG_TOKEN(UNAUTHORIZED, "wrong token"),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "expired access token"),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "expired access token"),
    NOT_FOUND_TOKEN(UNAUTHORIZED, "not found token"),
    NOT_EXPIRED_TOKEN(UNAUTHORIZED, "not expired yet"),

    // UserError
    DUPLICATED_NICKNAME(BAD_REQUEST, "duplicated nickname"),
    ALREADY_SIGN_UP(NOT_FOUND, "already sign up"),
    PROFILE_PHOTO_UPLOAD_EXCEPTION(NOT_FOUND, "this profile photo can not upload"),

    // UserEmailError
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "Duplicated email Request"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Email is not valid"),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "Bad Credentialsm"),

    // 5xx
    UNKNOWN_EXCEPTION(INTERNAL_SERVER_ERROR, "unknown_Exception")
    ;

    private final HttpStatus status;
    private final String message;
}

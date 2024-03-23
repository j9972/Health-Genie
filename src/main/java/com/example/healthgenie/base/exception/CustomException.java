package com.example.healthgenie.base.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public static final CustomException TRAINER_INFO_EMPTY = new CustomException(ErrorCode.TRAINER_INFO_EMPTY);
    public static final CustomException USER_EMPTY = new CustomException(ErrorCode.USER_EMPTY);
    public static final CustomException COMMENT_EMPTY = new CustomException(ErrorCode.COMMENT_EMPTY);
    public static final CustomException LIKE_EMPTY = new CustomException(ErrorCode.LIKE_EMPTY);
    public static final CustomException MATCHING_EMPTY = new CustomException(ErrorCode.MATCHING_EMPTY);
    public static final CustomException PHOTO_EMPTY = new CustomException(ErrorCode.PHOTO_EMPTY);
    public static final CustomException POST_EMPTY = new CustomException(ErrorCode.POST_EMPTY);
    public static final CustomException TODO_EMPTY = new CustomException(ErrorCode.TODO_EMPTY);
    public static final CustomException ROOM_EMPTY = new CustomException(ErrorCode.ROOM_EMPTY);
    public static final CustomException REFRESH_TOKEN_EMPTY = new CustomException(ErrorCode.REFRESH_TOKEN_EMPTY);
    public static final CustomException NO_PROCESS_HISTORY = new CustomException(ErrorCode.NO_PROCESS_HISTORY);
    public static final CustomException NO_REVIEW_HISTORY = new CustomException(ErrorCode.NO_REVIEW_HISTORY);
    public static final CustomException DUPLICATED_REVIEW = new CustomException(ErrorCode.DUPLICATED_REVIEW);
    public static final CustomException DUPLICATED_DAY = new CustomException(ErrorCode.DUPLICATED_DAY);
    public static final CustomException DUPLICATED_NICKNAME = new CustomException(ErrorCode.DUPLICATED_NICKNAME);
    public static final CustomException DUPLICATED_EMAIL = new CustomException(ErrorCode.DUPLICATED_EMAIL);
    public static final CustomException DUPLICATED_TRAINER_INFO = new CustomException(ErrorCode.DUPLICATED_TRAINER_INFO);
    public static final CustomException WRONG_DATE = new CustomException(ErrorCode.WRONG_DATE);
    public static final CustomException WRONG_USER_ROLE = new CustomException(ErrorCode.WRONG_USER_ROLE);
    public static final CustomException NOT_VALID_FIELD = new CustomException(ErrorCode.NOT_VALID_FIELD);
    public static final CustomException NOT_VALID_VALUE = new CustomException(ErrorCode.NOT_VALID_VALUE);
    public static final CustomException ALREADY_LIKED = new CustomException(ErrorCode.ALREADY_LIKED);
    public static final CustomException ALREADY_EXISTS_ROLE = new CustomException(ErrorCode.ALREADY_EXISTS_ROLE);
    public static final CustomException SELF_CHAT = new CustomException(ErrorCode.SELF_CHAT);
    public static final CustomException TOO_EARLY_TO_WRITE_FEEDBACK = new CustomException(ErrorCode.TOO_EARLY_TO_WRITE_FEEDBACK);
    public static final CustomException NO_JWT = new CustomException(ErrorCode.NO_JWT);
    public static final CustomException WRONG_SIGNATURE = new CustomException(ErrorCode.WRONG_SIGNATURE);
    public static final CustomException EXPIRED_TOKEN = new CustomException(ErrorCode.EXPIRED_TOKEN);
    public static final CustomException UNSUPPORTED = new CustomException(ErrorCode.UNSUPPORTED);
    public static final CustomException WRONG_TOKEN = new CustomException(ErrorCode.WRONG_TOKEN);
    public static final CustomException NOT_EXPIRED_TOKEN = new CustomException(ErrorCode.NOT_EXPIRED_TOKEN);
    public static final CustomException NO_PERMISSION = new CustomException(ErrorCode.NO_PERMISSION);

    public static final CustomException FAILED_PHOTO_UPLOAD = new CustomException(ErrorCode.FAILED_PHOTO_UPLOAD);
    public static final CustomException UNKNOWN_EXCEPTION = new CustomException(ErrorCode.UNKNOWN_EXCEPTION);

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

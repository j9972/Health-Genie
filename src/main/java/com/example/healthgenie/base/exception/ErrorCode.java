package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 4xx
    // ENTITY NOT FOUND
    TRAINER_INFO_EMPTY(BAD_REQUEST, "트레이너 정보가 존재하지 않습니다."),
    USER_EMPTY(BAD_REQUEST, "회원이 존재하지 않습니다."),
    COMMENT_EMPTY(BAD_REQUEST, "댓글이 존재하지 않습니다."),
    LIKE_EMPTY(BAD_REQUEST, "좋아요가 존재하지 않습니다."),
    MATCHING_EMPTY(BAD_REQUEST, "매칭이 존재하지 않습니다."),
    PHOTO_EMPTY(BAD_REQUEST, "사진이 존재하지 않습니다."),
    POST_EMPTY(BAD_REQUEST, "게시글이 존재하지 않습니다."),
    TODO_EMPTY(BAD_REQUEST, "할일이 존재하지 않습니다."),
    ROOM_EMPTY(BAD_REQUEST, "채팅방이 존재하지 않습니다."),
    REFRESH_TOKEN_EMPTY(BAD_REQUEST, "리프레시 토큰이 존재하지 않습니다."),

    // NO HISTORY
    NO_PROCESS_HISTORY(BAD_REQUEST, "일지를 남긴 기록이 없습니다."),
    NO_REVIEW_HISTORY(BAD_REQUEST, "후기를 남긴 기록이 없습니다."),

    // DUPLICATED
    DUPLICATED_REVIEW(BAD_REQUEST, "이미 같은 후기가 존재합니다."),
    DUPLICATED_DAY(BAD_REQUEST, "이미 같은 요일이 존재합니다."),
    DUPLICATED_NICKNAME(BAD_REQUEST, "이미 같은 닉네임이 존재합니다."),
    DUPLICATED_EMAIL(BAD_REQUEST, "이미 같은 이메일이 존재합니다."),
    DUPLICATED_TRAINER_INFO(BAD_REQUEST, "이미 같은 트레이너 정보가 존재합니다."),

    // WRONG
    WRONG_DATE(BAD_REQUEST, "날짜가 잘못되었습니다."),
    WRONG_USER_ROLE(BAD_REQUEST, "회원의 역할이 잘못되었습니다."),

    // NOT VALIDATED
    NOT_VALID_FIELD(BAD_REQUEST, "유효하지 않은 필드입니다."),
    NOT_VALID_VALUE(BAD_REQUEST, "유효하지 않은 값입니다."),

    // ALREADY
    ALREADY_LIKED(BAD_REQUEST, "이미 좋아요를 표시했습니다."),
    ALREADY_EXISTS_ROLE(BAD_REQUEST, "이미 역할을 선택했습니다."),

    SELF_CHAT(BAD_REQUEST, "자기 자신과 채팅은 불가능합니다."),
    TOO_EARLY_TO_WRITE_FEEDBACK(BAD_REQUEST, "아직 일지를 작성하기 이릅니다."),

    // JWT
    NO_JWT(UNAUTHORIZED, "요청 시, JWT를 함께 보내주세요."),
    WRONG_SIGNATURE(UNAUTHORIZED, "잘못된 서명입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNSUPPORTED(UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    WRONG_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다."),
    NOT_EXPIRED_TOKEN(UNAUTHORIZED, "아직 토큰이 만료되지 않았습니다."),

    NO_PERMISSION(BAD_REQUEST, "권한이 없습니다."),

    FAILED_PHOTO_UPLOAD(INTERNAL_SERVER_ERROR, "사진 업로드에 실패했습니다. 관리자에게 문의해주세요."),

    // 5xx
    UNKNOWN_EXCEPTION(INTERNAL_SERVER_ERROR, "알 수 없는 예외입니다. 관리자에게 문의해주세요.")
    ;

    private final HttpStatus status;
    private final String message;
}

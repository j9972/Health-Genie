package com.example.healthgenie.boundedContext.matching.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchingState {

    /*
    수락/거절/승인 Enum
     */
    PARTICIPATE("PARTICIPATE", "PT 참석"),
    CANCEL("CANCEL", "PT 취소"),
    PARTICIPATE_ACCEPT("PARTICIPATE_ACCEPT", "PT 참석 승인"),
    CANCEL_ACCEPT("CANCEL_ACCEPT", "PT 취소 승인"),
    DEFAULT("DEFAULT", "기본 값")
    ;

    private final String code;
    private final String name;

    @JsonCreator
    public static MatchingState from(String code) {
        for (MatchingState state : MatchingState.values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }
}

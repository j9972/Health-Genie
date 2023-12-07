package com.example.healthgenie.boundedContext.matching.entity;

import com.example.healthgenie.boundedContext.user.entity.Role;
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
    PARTICIPATE_ACCEPT("ACCEPT", "PT 승인"),
    CANCEL_ACCEPT("ACCEPT", "PT 승인"),
    DEFAULT("DEFAULT", "기본 값")
    ;

    private final String code;
    private final String name;

    @JsonCreator
    public static Role from(String code) {
        for (Role role : Role.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }
}

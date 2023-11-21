package com.example.healthgenie.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("ADMIN", "관리자 권한"),
    EMPTY("EMPTY", "기본 권한"),
    USER("USER", "일반 사용자 권한"),
    TRAINER("TRAINER", "트레이너 권한");

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

package com.example.healthgenie.boundedContext.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    UNKNOWN("UNKNOWN", "알수없음"),
    MALE("MALE", "남자"),
    FEMALE("FEMALE", "여자");

    private final String code;
    private final String name;

    @JsonCreator
    public static Gender from(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return null;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }
}

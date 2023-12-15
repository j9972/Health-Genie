package com.example.healthgenie.boundedContext.routine.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {

    EMPTY("", "기본"),
    BEGINNER("BEGINNER", "초급자"), //  초급자
    INTERMEDIATE("INTERMEDIATE", "중급자"), // 중급자
    EXPERT("EXPERT", "상급자"); // 상급자

    private final String code;
    private final String name;

    @JsonCreator
    public static Level from(String code) {
        for (Level level : Level.values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return null;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }
}

package com.example.healthgenie.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {


    ADMIN("ROLE_ADMIN", "관리자 권한"),
    USER("ROLE_USER", "일반 사용자 권한"),
    GUEST("ROLE_TRAINER", "트레이너 권한");

    private final String code;
    private final String name;
}

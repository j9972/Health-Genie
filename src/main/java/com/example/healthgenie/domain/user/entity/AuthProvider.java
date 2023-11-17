package com.example.healthgenie.domain.user.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AuthProvider {

    GOOGLE("google"),
    KAKAO("kakao"),
    EMPTY("");

    private String authProvider;

    AuthProvider(String authProvider){
        this.authProvider = authProvider;
    }

    public static AuthProvider findByCode(String code){
        return Arrays.stream(AuthProvider.values())
                .filter(provider -> provider.getAuthProvider().equalsIgnoreCase(code))
                .findFirst()
                .orElse(EMPTY);
    }
}

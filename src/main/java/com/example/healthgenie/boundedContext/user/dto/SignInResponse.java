package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResponse {

    private AuthProvider authProvider;
    private KakaoUserInfo kakaoUserInfo;
    private GoogleUserInfo googleUserInfo;
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Role role;
}
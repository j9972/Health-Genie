package com.example.healthgenie.domain.user.dto;

import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.domain.user.entity.Role;
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
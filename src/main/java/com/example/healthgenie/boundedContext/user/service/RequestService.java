package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.boundedContext.user.dto.SignInResponse;
import com.example.healthgenie.boundedContext.user.dto.TokenRequest;
import com.example.healthgenie.boundedContext.user.dto.TokenResponse;

public interface RequestService<T> {
    SignInResponse redirect(TokenRequest tokenRequest);
    TokenResponse getToken(TokenRequest tokenRequest);
    T getUserInfo(String accessToken);
    TokenResponse getRefreshToken(String provider, String refreshToken);
}
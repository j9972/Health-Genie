package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.SignInResponse;
import com.example.healthgenie.domain.user.dto.TokenRequest;
import com.example.healthgenie.domain.user.dto.TokenResponse;
import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.example.healthgenie.global.utils.SecurityUtils;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoRequestService kakaoRequestService;
    private final GoogleRequestService googleRequestService;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public SignInResponse redirect(TokenRequest tokenRequest){
        if(AuthProvider.KAKAO.getAuthProvider().equals(tokenRequest.getRegistrationId())){
            return kakaoRequestService.redirect(tokenRequest);
        } else if(AuthProvider.GOOGLE.getAuthProvider().equals(tokenRequest.getRegistrationId())) {
            return googleRequestService.redirect(tokenRequest);
        }

        throw new CommonException(CommonErrorResult.BAD_REQUEST);
    }

    public SignInResponse refreshToken(TokenRequest tokenRequest){
        String userId = (String) securityUtils.get(tokenRequest.getRefreshToken()).get("userId");
        String provider = (String) securityUtils.get(tokenRequest.getRefreshToken()).get("provider");
        String oldRefreshToken = (String) securityUtils.get(tokenRequest.getRefreshToken()).get("refreshToken");

        if(!userRepository.existsByEmailAndAuthProvider(userId, AuthProvider.findByCode(provider))){
            throw new CommonException(CommonErrorResult.BAD_REQUEST);
        }

        TokenResponse tokenResponse = null;
        if(AuthProvider.KAKAO.getAuthProvider().equals(provider.toLowerCase())){
            tokenResponse = kakaoRequestService.getRefreshToken(provider, oldRefreshToken);
        } else if(AuthProvider.GOOGLE.getAuthProvider().equals(provider.toLowerCase())){
            tokenResponse = googleRequestService.getRefreshToken(provider, oldRefreshToken);
        }

        String accessToken = securityUtils.createAccessToken(
                userId, AuthProvider.findByCode(provider.toLowerCase()), tokenResponse.getAccessToken());

        return SignInResponse.builder()
                .authProvider(AuthProvider.findByCode(provider.toLowerCase()))
                .kakaoUserInfo(null)
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
    }
}
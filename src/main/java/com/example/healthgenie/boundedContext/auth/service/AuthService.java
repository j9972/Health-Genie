package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.exception.ErrorCode;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.GOOGLE;
import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.KAKAO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final KakaoRequestService kakaoRequestService;
    private final GoogleRequestService googleRequestService;

    @Transactional
    public JwtResponse redirect(String provider, String code, String state) {
        if (Objects.equals(provider, KAKAO.getAuthProvider())) {
            return kakaoRequestService.call(code);
        } else if (Objects.equals(provider, GOOGLE.getAuthProvider())) {
            return googleRequestService.call(code);
        }

        throw new CustomException(ErrorCode.NOT_VALID, "["+provider+"]"+"는 잘못된 제공자입니다.");
    }
}

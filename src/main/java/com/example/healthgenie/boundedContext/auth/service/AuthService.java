package com.example.healthgenie.boundedContext.auth.service;

import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.GOOGLE;
import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.KAKAO;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.exception.ErrorCode;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final KakaoRequestService kakaoRequestService;
    private final GoogleRequestService googleRequestService;
    private final UserService userService;

    @Transactional
    public JwtResponse redirect(String provider, String code, String state) {
        if (Objects.equals(provider, KAKAO.getAuthProvider())) {
            return kakaoRequestService.call(code);
        } else if (Objects.equals(provider, GOOGLE.getAuthProvider())) {
            return googleRequestService.call(code);
        }

        throw new CustomException(ErrorCode.NOT_VALID, "[" + provider + "]" + "는 잘못된 제공자입니다.");
    }

    @Transactional
    public void withdraw(User user, String accessToken) {
        AuthProvider provider = user.getAuthProvider();

        userService.deleteById(user.getId());

        if (Objects.equals(provider, KAKAO)) {
            kakaoRequestService.unlink(accessToken);
        } else if (Objects.equals(provider, GOOGLE)) {
            googleRequestService.unlink(accessToken);
        }
    }
}

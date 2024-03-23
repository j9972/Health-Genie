package com.example.healthgenie.boundedContext.user.service;


import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.base.utils.JwtTokenProvider;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.dto.JwtResponse;
import com.example.healthgenie.boundedContext.user.dto.TokenRequest;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.KAKAO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final KakaoRequestService kakaoRequestService;
    private final GoogleRequestService googleRequestService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtResponse redirect(TokenRequest tokenRequest) {
        if (KAKAO.getAuthProvider().equals(tokenRequest.getRegistrationId())) {
            return kakaoRequestService.getToken(tokenRequest);
        } else if (AuthProvider.GOOGLE.getAuthProvider().equals(tokenRequest.getRegistrationId())) {
            return googleRequestService.getToken(tokenRequest);
        }

        throw CustomException.NOT_VALID_FIELD;
    }

    @Transactional
    public JwtResponse refreshToken(String accessToken, String refreshToken) {
        if (!jwtTokenProvider.isExpired(accessToken)) {
            throw CustomException.NOT_EXPIRED_TOKEN;
        }

        RefreshToken refreshTokenObj = refreshTokenService.findByRefreshToken(refreshToken);

        User user = userService.findByEmail(refreshTokenObj.getKeyEmail());

        if (jwtTokenProvider.isExpired(refreshToken)) {
            refreshTokenService.deleteByKeyEmail(user.getEmail());
            refreshTokenObj = refreshTokenService.save(
                    jwtTokenProvider.generateRefreshToken(user.getEmail(), user.getRole().getCode()), user.getEmail());
        }

        return JwtResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .accessToken(jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole().getCode()))
                .refreshToken(refreshTokenObj.getRefreshToken())
                .build();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityUtils.getCurrentUser();

        CookieUtils.deleteCookie(request, response, "AccessToken");
    }
}
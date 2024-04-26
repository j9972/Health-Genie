package com.example.healthgenie.boundedContext.auth.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.exception.ErrorCode;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.auth.dto.JwtResponse;
import com.example.healthgenie.boundedContext.auth.dto.OAuthTokenResponse;
import com.example.healthgenie.boundedContext.auth.dto.UserInfo;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.example.healthgenie.base.constant.Constants.ACCESS_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.base.constant.Constants.REFRESH_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.GOOGLE;
import static com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider.KAKAO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final KakaoRequestService kakaoRequestService;
    private final GoogleRequestService googleRequestService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @Transactional
    public JwtResponse getJwt(String provider, String code, String state) {
        OAuthTokenResponse oauthToken;
        UserInfo userInfo = switch (provider) {
            case "kakao" -> {
                oauthToken = kakaoRequestService.getAccessToken(code);
                yield kakaoRequestService.getUserInfo(oauthToken.getAccessToken());
            }
            case "google" -> {
                oauthToken = googleRequestService.getAccessToken(code);
                yield googleRequestService.getUserInfo(oauthToken.getAccessToken());
            }
            default -> throw new CustomException(ErrorCode.NOT_VALID, "[" + provider + "]" + "는 잘못된 제공자입니다.");
        };

        User user = getUser(userInfo, provider);

        String access = jwtUtils.createJwt("access", user.getEmail(), user.getRole().getCode(), ACCESS_TOKEN_EXPIRATION_MS);
        String refresh = jwtUtils.createJwt("refresh", user.getEmail(), user.getRole().getCode(), REFRESH_TOKEN_EXPIRATION_MS);

        refreshTokenService.save(refresh, user.getEmail(), REFRESH_TOKEN_EXPIRATION_MS);

        return JwtResponse.builder()
                .userId(user.getId())
                .role(user.getRole())
                .accessToken(access)
                .refreshToken(refresh)
                .oauthAccessToken(oauthToken.getAccessToken())
                .build();
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

    private User getUser(UserInfo userInfo, String provider) {
        AuthProvider authProvider = provider.equals("kakao") ? KAKAO : GOOGLE;
        if(!userService.existsByEmail(userInfo.getEmail())) {
            return userService.signUp(userInfo.getEmail(), userInfo.getName(), authProvider);
        }

        return userService.findByEmail(userInfo.getEmail());
    }
}

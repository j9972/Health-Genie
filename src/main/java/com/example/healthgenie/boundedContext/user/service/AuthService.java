package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.base.utils.JwtTokenProvider;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.dto.SignInResponse;
import com.example.healthgenie.boundedContext.user.dto.Token;
import com.example.healthgenie.boundedContext.user.dto.TokenRequest;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;
import static com.example.healthgenie.boundedContext.user.entity.AuthProvider.KAKAO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final KakaoRequestService kakaoRequestService;
    private final GoogleRequestService googleRequestService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignInResponse redirect(TokenRequest tokenRequest){
        if(KAKAO.getAuthProvider().equals(tokenRequest.getRegistrationId())){
            return kakaoRequestService.redirect(tokenRequest);
        } else if(AuthProvider.GOOGLE.getAuthProvider().equals(tokenRequest.getRegistrationId())) {
            return googleRequestService.redirect(tokenRequest);
        }

        throw new CommonException(CommonErrorResult.BAD_REQUEST);
    }

    @Transactional
    public SignInResponse refreshToken(String accessToken, String refreshToken) {
        if(!jwtTokenProvider.isExpired(accessToken)) {
            throw new JwtException(JwtErrorResult.NOT_EXPIRED_TOKEN);
        }

        RefreshToken refreshTokenObj = refreshTokenService.findByRefreshToken(refreshToken);

        User user = userRepository.findByEmail(refreshTokenObj.getKeyEmail())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if(!jwtTokenProvider.isExpired(refreshToken)) {
            return SignInResponse.builder()
                    .authProvider(user.getAuthProvider())
                    .accessToken(jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode()).getAccessToken())
                    .refreshToken(refreshToken)
                    .build();
        } else {
            Token newToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode());

            refreshTokenService.save(newToken.getRefreshToken(), user.getEmail());

            return SignInResponse.builder()
                    .authProvider(user.getAuthProvider())
                    .accessToken(newToken.getAccessToken())
                    .refreshToken(newToken.getRefreshToken())
                    .build();
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityUtils.getCurrentUser();

        CookieUtils.deleteCookie(request, response, "AccessToken");
    }
}
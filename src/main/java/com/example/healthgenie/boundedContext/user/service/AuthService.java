package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.UserException;
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
import static com.example.healthgenie.boundedContext.user.entity.AuthProvider.GOOGLE;
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
    public SignInResponse refreshToken(TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();

        RefreshToken refreshTokenObj = refreshTokenService.findByRefreshToken(refreshToken);

        User user = userRepository.findByEmail(refreshTokenObj.getKeyEmail())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        AuthProvider authProvider = tokenRequest.getRegistrationId().equalsIgnoreCase(KAKAO.getAuthProvider()) ? KAKAO : GOOGLE;

        // 유효한 리프레시 토큰이면, 새로운 액세스 토큰 생성 및 반환
        if(jwtTokenProvider.validateRefreshToken(refreshTokenObj)) {
            log.info("유효한 Refresh Token 입니다.");
            return SignInResponse.builder()
                    .authProvider(authProvider)
                    .accessToken(jwtTokenProvider.recreationAccessToken(user.getEmail(), user.getRole().getCode()))
                    .refreshToken(refreshTokenObj.getRefreshToken())
                    .build();
        }
        // 유효하지 않은 리프레시 토큰이면, 새로운 토큰 생성 및 반환
        else {
            log.info("유효하지 않은 Refresh Token 입니다.");
            Token newToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().getCode());

            refreshTokenService.save(newToken.getRefreshToken(), user.getEmail());

            return SignInResponse.builder()
                    .authProvider(authProvider)
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
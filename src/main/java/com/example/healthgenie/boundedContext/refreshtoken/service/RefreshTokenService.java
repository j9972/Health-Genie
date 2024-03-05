package com.example.healthgenie.boundedContext.refreshtoken.service;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.exception.JwtException;
import com.example.healthgenie.boundedContext.refreshtoken.entity.RefreshToken;
import com.example.healthgenie.boundedContext.refreshtoken.repository.RefreshTokenRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.healthgenie.base.exception.JwtErrorResult.NOT_FOUND_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public RefreshToken save(String refreshToken, String email) {
        Optional<User> opUser = userRepository.findByEmail(email);
        if(opUser.isEmpty()) {
            throw new CommonException(CommonErrorResult.BAD_REQUEST);
        }

        // 기존의 만료된 리프레시 토큰 삭제
        if(refreshTokenRepository.existsByKeyEmail(email)) {
            refreshTokenRepository.deleteByKeyEmail(email);
        }

        RefreshToken rt = RefreshToken.builder()
                .keyEmail(email)
                .refreshToken(refreshToken)
                .build();

        return refreshTokenRepository.save(rt);
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new JwtException(NOT_FOUND_TOKEN));
    }
}
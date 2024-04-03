package com.example.healthgenie.boundedContext.auth;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.refreshtoken.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtUtils jwtUtils;

    public TokenResponse getTokens(Cookie[] cookies) {
        String access = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("access"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> CustomException.NOT_VALID_FIELD);

        String refresh = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("refresh"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> CustomException.NOT_VALID_FIELD);

        String category1 = jwtUtils.getCategory(access);
        String category2 = jwtUtils.getCategory(refresh);

        if(!category1.equals("access") || !category2.equals("refresh")) {
            throw CustomException.NOT_VALID_VALUE;
        }

        return TokenResponse.builder()
                .access(access)
                .refresh(refresh)
                .build();
    }
}

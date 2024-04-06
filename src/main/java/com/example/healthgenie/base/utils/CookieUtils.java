package com.example.healthgenie.base.utils;

import com.example.healthgenie.base.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

    public static Cookie getCookie(HttpServletRequest request, String key) {
        if(request.getCookies() == null) {
            throw new CustomException(DATA_NOT_FOUND, "요청에 쿠키가 비어있습니다.");
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .findAny()
                .orElseGet(() -> new Cookie(key, ""));
    }

    public static Cookie deleteCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }
}
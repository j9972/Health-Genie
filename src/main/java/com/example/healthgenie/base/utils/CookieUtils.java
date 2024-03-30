package com.example.healthgenie.base.utils;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

    public static Cookie createCookie(String key, String value) {
        return createCookie(key, value, false);
    }

    public static Cookie createCookie(String key, String value, boolean ssl) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(ssl);

        return cookie;
    }
}
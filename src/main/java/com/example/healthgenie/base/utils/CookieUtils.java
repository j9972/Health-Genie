package com.example.healthgenie.base.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

    public static Cookie createCookie(String key, String value) {
        return createCookie(key, value, null);
    }

    public static Cookie createCookie(String key, String value, boolean ssl) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(ssl);

        return cookie;
    }

    public static Cookie createCookie(String key, String value, String domain) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
//        cookie.setDomain(null);
        cookie.setSecure(false);

        return cookie;
    }

    public static Cookie getCookie(HttpServletRequest request, String key) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .findAny()
                .orElse(null);
    }

    public static Cookie deleteCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }
}
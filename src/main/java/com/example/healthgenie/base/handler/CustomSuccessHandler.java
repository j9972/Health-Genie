package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.constant.Constants;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.auth.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = customOAuth2User.getEmail();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String access = jwtUtils.createJwt(email, role, Constants.ACCESS_TOKEN_EXPIRE_COUNT);
        String refresh = jwtUtils.createJwt(email, role, Constants.REFRESH_TOKEN_EXPIRE_COUNT);

        response.setHeader("Authorization", access);
        response.addCookie(createCookie("refresh", refresh));
//        response.sendRedirect("http://localhost:3000/health-management");
        response.sendRedirect("http://localhost:1234/");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);

        return cookie;
    }
}

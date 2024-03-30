package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.auth.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.healthgenie.base.constant.Constants.*;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = customOAuth2User.getEmail();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String access = jwtUtils.createJwt(email, role, ACCESS_TOKEN_EXPIRE_COUNT);
        String refresh = jwtUtils.createJwt(email, role, REFRESH_TOKEN_EXPIRE_COUNT);

        response.setHeader("Authorization", BEARER_PREFIX + access);
        response.addCookie(CookieUtils.createCookie("refresh", refresh));
//        response.sendRedirect("http://localhost:3000/health-management");
        response.sendRedirect("http://localhost:1234/");
    }
}

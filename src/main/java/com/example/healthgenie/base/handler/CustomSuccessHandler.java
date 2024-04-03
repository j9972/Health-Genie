package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.utils.CookieUtils;
import com.example.healthgenie.base.utils.JwtUtils;
import com.example.healthgenie.boundedContext.refreshtoken.service.RefreshTokenService;
import com.example.healthgenie.boundedContext.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.healthgenie.base.constant.Constants.ACCESS_TOKEN_EXPIRATION_MS;
import static com.example.healthgenie.base.constant.Constants.REFRESH_TOKEN_EXPIRATION_MS;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String email = user.getEmail();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String access = jwtUtils.createJwt("access", email, role, ACCESS_TOKEN_EXPIRATION_MS);
        String refresh = jwtUtils.createJwt("refresh", email, role, REFRESH_TOKEN_EXPIRATION_MS);
        log.info("[인증 성공]");
        log.info("[ACCESS TOKEN]={}", access);
        log.info("[REFRESH TOKEN]={}", refresh);

        refreshTokenService.save(refresh, email, REFRESH_TOKEN_EXPIRATION_MS);

        response.addCookie(CookieUtils.createCookie("access", access));
        response.addCookie(CookieUtils.createCookie("refresh", refresh));
        String url = "https://xn--9w3b15cw7a.xn--3e0b707e/token";
        if(request.getRequestURL().toString().contains("localhost")) {
            url = "http://localhost:1234/token";
        }
        response.sendRedirect(url);
    }
}

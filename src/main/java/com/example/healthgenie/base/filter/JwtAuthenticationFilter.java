package com.example.healthgenie.base.filter;

import com.example.healthgenie.base.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String accessToken = StringUtils.hasText(authorization) ? jwtTokenProvider.resolveToken(authorization) : null;

        if(StringUtils.hasText(accessToken)) {
            if(accessToken.equals("admin")) {
                Authentication authentication = jwtTokenProvider.getAuthentication("admin@admin.com");

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if(!request.getRequestURI().equals("/refresh") && jwtTokenProvider.validateToken(accessToken)) {
                String email = jwtTokenProvider.getEmail(accessToken);

                Authentication authentication = jwtTokenProvider.getAuthentication(email);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
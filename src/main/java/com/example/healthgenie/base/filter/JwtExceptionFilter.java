package com.example.healthgenie.base.filter;

import com.example.healthgenie.base.exception.JwtException;
import com.example.healthgenie.base.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String, String> map = new HashMap<>();
            map.put("code", String.valueOf(e.getJwtErrorResult()));
            map.put("message", e.getJwtErrorResult().getMessage());

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(e.getJwtErrorResult().getHttpStatus().value());
            response.getWriter().print(objectMapper.writeValueAsString(map));
        } catch (UserException e) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "USER_NOT_FOUND");
            map.put("message", "user not found");

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(e.getUserErrorResult().getHttpStatus().value());
            response.getWriter().print(objectMapper.writeValueAsString(map));
        }
    }
}

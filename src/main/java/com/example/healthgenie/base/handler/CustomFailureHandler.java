package com.example.healthgenie.base.handler;

import com.example.healthgenie.base.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.DUPLICATED;
        StackTraceElement element = exception.getStackTrace()[0];

        Map<String, String> map = new HashMap<>();
        map.put("code", errorCode.name());
        map.put("message", errorCode.getMessage() + " : " + exception.getMessage());
        log.warn("[{}] occurs caused by {}.{}() {} line : {}", errorCode.name(), element.getClassName(), element.getMethodName(), element.getLineNumber(), exception.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().print(objectMapper.writeValueAsString(map));
        response.sendRedirect("http://localhost:3000/login-failure");
    }
}

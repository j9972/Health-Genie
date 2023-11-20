package com.example.healthgenie.global.config.auth;

import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.global.utils.SecurityUtils;
import com.example.healthgenie.repository.UserRepository;
import com.google.gson.JsonIOException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        log.info("\n**** SECURITY FILTER START");

        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token;
            String email;
            String provider;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);

                // 더미 데이터용
                // testaccesstoken_[테스트 계정 이메일]
                if(token.startsWith("testaccesstoken_")) {
                    Authentication dummy = securityUtils.createDummy(token.substring(16));
                    SecurityContextHolder.getContext().setAuthentication(dummy);
                    filterChain.doFilter(request, response);
                }

                if (securityUtils.isExpiration(token)) { // 만료되었는지 체크
                    throw new JwtException("EXPIRED_ACCESS_TOKEN");
                }

                email = (String) securityUtils.get(token).get("email");
                provider = (String) securityUtils.get(token).get("provider");

                if(!userRepository.existsByEmailAndAuthProvider(email, AuthProvider.findByCode(provider))){
                    throw new JwtException("CANNOT_FOUND_USER");
                }

                Authentication authentication = securityUtils.parseAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            if (e.getMessage().equalsIgnoreCase("EXPIRED_ACCESS_TOKEN")) {
                writeErrorLogs("EXPIRED_ACCESS_TOKEN", e.getMessage(), e.getStackTrace());
                JSONObject jsonObject = createJsonError(String.valueOf(UNAUTHORIZED.value()), e.getMessage());
                setJsonResponse(response, UNAUTHORIZED, jsonObject.toString());
            } else if (e.getMessage().equalsIgnoreCase("CANNOT_FOUND_USER")) {
                writeErrorLogs("CANNOT_FOUND_USER", e.getMessage(), e.getStackTrace());
                JSONObject jsonObject = createJsonError(String.valueOf(UNAUTHORIZED.value()), e.getMessage());
                setJsonResponse(response, UNAUTHORIZED, jsonObject.toString());
            }
        } catch (Exception e) {
            writeErrorLogs("Exception", e.getMessage(), e.getStackTrace());

            if (response.getStatus() == HttpStatus.OK.value()) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } finally {
            log.info("**** SECURITY FILTER FINISH\n");
        }
    }

    private JSONObject createJsonError(String errorCode, String errorMessage) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("error_code", errorCode);
            jsonObject.put("error_message", errorMessage);
        } catch (JsonIOException ex) {
            writeErrorLogs("JSONException", ex.getMessage(), ex.getStackTrace());
        }

        return jsonObject;
    }

    private void setJsonResponse(HttpServletResponse response, HttpStatus httpStatus, String jsonValue) {
        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        try {
            response.getWriter().write(jsonValue);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException ex) {
            writeErrorLogs("IOException", ex.getMessage(), ex.getStackTrace());
        }
    }

    private void writeErrorLogs(String exception, String message, StackTraceElement[] stackTraceElements) {
        log.error("**** " + exception + " ****");
        log.error("**** error message : " + message);
        log.error("**** stack trace : " + Arrays.toString(stackTraceElements));
    }

}
package com.example.healthgenie.base.utils;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.healthgenie.base.exception.ErrorCode.DATA_NOT_FOUND;


@Slf4j
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new CustomException(DATA_NOT_FOUND, "사용자 인증이 되지 않았습니다.");
        }
        return (User) authentication.getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentNickname() {
        return getCurrentUser().getNickname();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
}
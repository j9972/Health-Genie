package com.example.healthgenie.base.utils;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Slf4j
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static User getCurrentUser() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw CustomException.USER_EMPTY;
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
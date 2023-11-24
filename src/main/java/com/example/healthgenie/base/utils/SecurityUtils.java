package com.example.healthgenie.base.utils;

import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.healthgenie.base.exception.CommonErrorResult.USER_NOT_FOUND;

public class SecurityUtils {

    private SecurityUtils() { }

    public static User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new CommonException(USER_NOT_FOUND);
        }
        return (User) authentication.getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
}
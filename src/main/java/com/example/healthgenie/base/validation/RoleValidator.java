package com.example.healthgenie.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RoleValidator implements ConstraintValidator<RoleConstraint, String> {
    private static final Set<String> VALID_ROLES = Set.of("USER", "TRAINER", "ADMIN", "EMPTY");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return VALID_ROLES.contains(value.toUpperCase());
    }
}
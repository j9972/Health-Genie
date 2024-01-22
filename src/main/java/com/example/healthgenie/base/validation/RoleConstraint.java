package com.example.healthgenie.base.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface RoleConstraint {
    String message() default "invalid role value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
package com.example.healthgenie.base.exception.Diet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DietException extends RuntimeException {
    private final DietErrorResult dietErrorResult;
}

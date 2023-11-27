package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoutineException extends RuntimeException{
    private final RoutineErrorResult routineErrorResult;
}

package com.example.healthgenie.base.exception.Jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtException extends RuntimeException {
    private final JwtErrorResult jwtErrorResult;
}

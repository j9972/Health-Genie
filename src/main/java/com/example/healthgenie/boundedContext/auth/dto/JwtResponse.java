package com.example.healthgenie.boundedContext.auth.dto;

import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private Long userId;
    private Role role;
    private String accessToken;
    private String refreshToken;
}

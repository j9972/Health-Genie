package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private Long userIdasdf;
    private Role role;
    private String accessToken;
    private String refreshToken;
}

package com.example.healthgenie.boundedContext.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long user_id;
    private String email;
}
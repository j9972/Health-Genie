package com.example.healthgenie.domain.user.dto;

import com.example.healthgenie.domain.user.entity.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String email;
    private String nickname;
    private AuthProvider authProvider;
}
package com.example.healthgenie.domain.user.dto;

import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TestSignUpRequest {

    private String email;
    private String nickname;
    private AuthProvider authProvider;
    private Role role;
}
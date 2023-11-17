package com.example.healthgenie.domain.user.dto;

import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TestSignUpResponse {

    private Long id;
    private String email;
    private String nickname;
    private AuthProvider authProvider;
    private Role role;
    private LocalDateTime createdDate;
}
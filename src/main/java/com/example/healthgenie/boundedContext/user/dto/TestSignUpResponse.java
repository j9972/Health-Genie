package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
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
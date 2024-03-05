package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    private Role role;
    private String nickname;
}

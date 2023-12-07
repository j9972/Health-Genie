package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String email;
    private String uniName;
    private String name;
    private String nickname;
    private AuthProvider authProvider;
    private Role role;
    private String profilePhoto;
    private boolean emailVerify;
    private Level level;
}

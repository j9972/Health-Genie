package com.example.healthgenie.boundedContext.user.dto;


import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    private String email;
    private String name;
    private String uniName;
    private Role role;
    private AuthProvider authProvider;
    private Level level;
}
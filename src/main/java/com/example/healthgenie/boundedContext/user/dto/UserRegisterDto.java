package com.example.healthgenie.boundedContext.user.dto;


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

    /*
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .uniName(uniName)
                .name(name)
                .role(role)
                .build();
    }
    public User tosocialEntity() {
        return User.builder()
                .email(email)
                .uniName(uniName)
                .name(name)
                .provider(provider)
                .role(role)
                .build();
    }

     */
}
package com.example.healthgenie.domain.user.dto;


import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    private String email;
    private String password;
    private String name;
    private String uniName;
    private Role role;
    private String provider;
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
}
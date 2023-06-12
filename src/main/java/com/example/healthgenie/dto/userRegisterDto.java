package com.example.healthgenie.dto;

import com.example.healthgenie.entity.Role;
import com.example.healthgenie.entity.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class userRegisterDto {
    private String email;
    private String password;
    private String name;
    private String uniName;
    private Role role;
    private String provider;


    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .uniName(uniName)
                .name(name)
                .role(role)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .uniName(uniName)
                .name(name)
                .provider(provider)
                .role(role)
                .build();
    }
}

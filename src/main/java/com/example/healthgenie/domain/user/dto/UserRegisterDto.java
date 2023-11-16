package com.example.healthgenie.domain.user.dto;


import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.domain.user.entity.Role;
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
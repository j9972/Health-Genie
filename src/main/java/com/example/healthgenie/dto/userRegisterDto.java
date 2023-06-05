package com.example.healthgenie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class userRegisterDto {
    private String email;
    private String password;
    private String name;
    private String uniName;
}

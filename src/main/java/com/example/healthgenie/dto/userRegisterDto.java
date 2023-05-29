package com.example.healthgenie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class userRegisterDto {
    private String email;
    private String password;
    private String name;
    private String uniName;
}

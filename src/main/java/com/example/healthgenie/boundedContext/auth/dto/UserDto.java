package com.example.healthgenie.boundedContext.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String role;
    private String name;
    private String email;
}
package com.example.healthgenie.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class pwdFindRequestDto {
    String name;
    String email;
}

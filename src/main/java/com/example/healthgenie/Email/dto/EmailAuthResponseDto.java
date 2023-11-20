package com.example.healthgenie.Email.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailAuthResponseDto {

    public Long id;
    public String email;
    public String code;
}

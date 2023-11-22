package com.example.healthgenie.boundedContext.email.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailAuthResponseDto {

    public Long id;
    public String email;
    public String code;
}

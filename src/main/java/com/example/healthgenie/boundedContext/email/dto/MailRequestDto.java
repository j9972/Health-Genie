package com.example.healthgenie.boundedContext.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailRequestDto {
    private String univ_email;
    private int code;
    private String univName;
}
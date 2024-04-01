package com.example.healthgenie.boundedContext.refreshtoken.dto;

import lombok.Data;

@Data
public class TokenResponse {

    private String access;
    private String refresh;
}

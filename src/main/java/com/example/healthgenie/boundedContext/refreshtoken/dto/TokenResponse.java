package com.example.healthgenie.boundedContext.refreshtoken.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {

    private String access;
    private String refresh;
}

package com.example.healthgenie.boundedContext.auth.dto;

public interface OAuth2Response {

    String getProvider();

    String getEmail();

    String getName();
}

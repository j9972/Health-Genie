package com.example.healthgenie.domain.user.dto;

import com.example.healthgenie.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequest {

    private Role role;
}

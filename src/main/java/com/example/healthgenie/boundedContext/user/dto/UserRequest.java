package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.enums.Gender;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String uniName;
    @Size(min = 2, max = 8)
    private String nickname;
    private Role role;
    private Gender gender;
    private Boolean emailVerify;
    private Level level;
    private Double height;
    private LocalDateTime birth;
    private Double weight;
    private Double muscleWeight;
}

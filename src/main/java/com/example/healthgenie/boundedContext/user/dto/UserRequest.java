package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.enums.Gender;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private Role role;

    private Level level;

    private MultipartFile photo;
    @Size(min = 2, max = 8)
    private String nickname;
    private Gender gender;
    private LocalDateTime birth;
    private Double height;
    private Double weight;
    private Double muscleWeight;

    private String uniName;
    private Boolean emailVerify;
}

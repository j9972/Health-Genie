package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.base.validation.RoleConstraint;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Gender;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String email;
    private String uniName;
    private String name;
    @Size(min = 2, max = 8)
    private String nickname;
    private AuthProvider authProvider;
    @RoleConstraint(message = "잘못된 역할을 입력하였습니다.")
    private String role;
    private MultipartFile profilePhoto;
    private Boolean emailVerify;
    private Level level;
    private Double height;
    private String birth;
    private Double weight;
    private Double muscleWeight;
    private Gender gender;
}

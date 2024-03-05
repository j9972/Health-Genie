package com.example.healthgenie.boundedContext.user.dto;

import com.example.healthgenie.base.utils.DateUtils;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Gender;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String uniName;
    private String name;
    private String nickname;
    private AuthProvider authProvider;
    private Role role;
    private Level level;
    private String profilePhoto;
    private boolean emailVerify;
    private Double height;
    private String birth;
    private Double weight;
    private Double muscleWeight;
    private Gender gender;

    public static UserResponse of(User user) {
        String birth = "";
        if(user.getBirth() != null) {
            birth = DateUtils.toStringDate(user.getBirth());
        }
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .uniName(user.getUniName())
                .name(user.getName())
                .nickname(user.getNickname())
                .authProvider(user.getAuthProvider())
                .role(user.getRole())
                .level(user.getLevel())
                .profilePhoto(user.getProfilePhoto())
                .emailVerify(user.isEmailVerify())
                .height(user.getHeight())
                .birth(birth)
                .weight(user.getWeight())
                .muscleWeight(user.getMuscleWeight())
                .gender(user.getGender())
                .build();
    }

    public static List<UserResponse> of(List<User> users) {
        return users.stream()
                .map(UserResponse::of)
                .toList();
    }

    public static User toEntity(UserResponse response) {
        return User.builder()
                .id(response.getId())
                .email(response.getEmail())
                .uniName(response.getUniName())
                .name(response.getName())
                .nickname(response.getNickname())
                .authProvider(response.getAuthProvider())
                .role(response.getRole())
                .level(response.getLevel())
                .profilePhoto(response.getProfilePhoto())
                .emailVerify(response.isEmailVerify())
                .build();
    }
}

package com.example.healthgenie.boundedContext.user.entity;

import com.example.healthgenie.base.entity.BaseEntity;
import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.base.exception.ErrorCode;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Gender;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_TB")
@Builder(toBuilder = true)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "uniname")
    private String uniName;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(min = 2, max = 8)
    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "email_verify")
    private boolean emailVerify;

    // level field 추
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Column(name = "height")
    private Double height;

    @Column(name = "birth")
    private LocalDateTime birth;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "muscle_weight")
    private Double muscleWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    public void updateRole(Role role) {
        if (this.role != Role.EMPTY) {
            throw new CustomException(ErrorCode.NO_PERMISSION, "이미 역할을 선택했습니다.");
        }
        this.role = role;
    }

    public void updateLevel(Level level) {
        this.level = level;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateUniname(String uniname) {
        this.uniName = uniname;
    }

    public void updateEmailVerify(boolean emailVerify) {
        this.emailVerify = emailVerify;
    }

    public void updateProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void updateHeight(Double height) {
        this.height = height;
    }

    public void updateBirth(LocalDateTime birth) {
        this.birth = birth;
    }

    public void updateWeight(Double weight) {
        this.weight = weight;
    }

    public void updateMuscleWeight(Double muscleWeight) {
        this.muscleWeight = muscleWeight;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.getCode();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
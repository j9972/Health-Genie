package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.DietResponse;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Gender;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import static com.example.healthgenie.base.exception.UserErrorResult.*;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3UploadUtils s3UploadUtils;

    @Transactional
    public UserResponse signUp(String email, String name, AuthProvider authProvider, Role role) {
        String defaultNickname = createUniqueNickname();

        User user = User.builder()
                .email(email)
                .name(name)
                .nickname(defaultNickname)
                .authProvider(authProvider)
                .uniName("")
                .role(role)
                .level(Level.EMPTY)
                .build();

        return UserResponse.of(userRepository.save(user));
    }

    @Transactional
    public UserResponse signUp(String email, String name, AuthProvider authProvider) {
        return signUp(email, name, authProvider, Role.EMPTY);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional
    public UserResponse edit(Long userId, UserRequest request) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // 이메일
        if(StringUtils.hasText(request.getEmail())) {
            user.updateEmail(request.getEmail());
        }
        // 학교 이름
        if(StringUtils.hasText(request.getUniName())) {
            user.updateUniname(request.getUniName());
        }
        // 이름
        if(StringUtils.hasText(request.getName())) {
            user.updateName(request.getName());
        }
        // 닉네임
        if(StringUtils.hasText(request.getNickname())) {
            if(userRepository.existsByNickname(request.getNickname())) {
                throw new UserException(DUPLICATED_NICKNAME);
            }
            user.updateNickname(request.getNickname());
        }
        // 제공자
        if(request.getAuthProvider() != null && StringUtils.hasText(request.getAuthProvider().getAuthProvider())) {
            user.updateAuthProvider(request.getAuthProvider());
        }
        // 역할
        if(request.getRole() != null && StringUtils.hasText(request.getRole())) {
            user.updateRole(Role.valueOf(request.getRole().toUpperCase()));
        }
        // 프로필 사진
        if(request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
            String profilePhoto = uploadAndDelete(request.getProfilePhoto(), user.getProfilePhoto());
            user.updateProfilePhoto(profilePhoto);
        }
        // 이메일 인증 확인
        if(Objects.nonNull(request.getEmailVerify()) && request.getEmailVerify()) {
            user.updateEmailVerify(true);
        }
        // 단계
        if(request.getLevel() != null && StringUtils.hasText(request.getLevel().getCode())) {
            user.updateLevel(request.getLevel());
        }
        // 키
        if(Objects.nonNull(request.getHeight())) {
            user.updateHeight(request.getHeight());
        }
        // 생년월일
        if(StringUtils.hasText(request.getBirth())) {
            user.updateBirth(request.getBirth());
        }
        // 몸무게
        if(Objects.nonNull(request.getWeight())) {
            user.updateWeight(request.getWeight());
        }
        // 골격근량
        if(Objects.nonNull(request.getMuscleWeight())) {
            user.updateMuscleWeight(request.getMuscleWeight());
        }
        // 성별
        if(request.getGender() != null && StringUtils.hasText(request.getGender().getCode())) {
            user.updateGender(request.getGender());
        }

        return UserResponse.of(user);
    }

    public DietResponse calculate(Long userId, Integer type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Gender gender = user.getGender();
        double weight = user.getWeight();
        double height = user.getHeight();
        int age = LocalDateTime.now().getYear() - user.getBirth().getYear() + 1;

        double basic;
        if(gender == Gender.MALE) {
            basic = 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
        } else if(gender == Gender.FEMALE) {
            basic = 655 + (9.6 * weight) + (1.7 * height) - (4.7 * age);
        } else {
            throw new UserException(NOT_VALID_FIELD);
        }

        double active = switch (type) {
            case 1 -> basic * 1.2;
            case 2 -> basic * 1.4;
            case 3 -> basic * 1.6;
            case 4 -> basic * 1.8;
            default -> throw new UserException(NOT_VALID_FIELD);
        };

        return DietResponse.builder()
                .basicRate((int) Math.round(basic))
                .activeRate((int) Math.round(active))
                .build();
    }

    private String createUniqueNickname() {
        String nickname;

        Random random = new Random();
        while(true) {
            String temp = String.valueOf(random.nextInt(99999999) + 1);

            if (!userRepository.existsByNickname(temp)) {
                nickname = temp;
                break;
            }
        }

        return nickname;
    }

    private String uploadAndDelete(MultipartFile uploadPhoto, String deletePhotoPath) throws IOException {
        String uploadedPath = s3UploadUtils.upload(uploadPhoto, "profile-photo");

        if(StringUtils.hasText(deletePhotoPath)) {
            s3UploadUtils.deleteS3Object("profile-photo", deletePhotoPath);
        }

        return uploadedPath;
    }
}
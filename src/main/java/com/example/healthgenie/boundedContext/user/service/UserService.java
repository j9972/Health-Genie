package com.example.healthgenie.boundedContext.user.service;

import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
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
import java.util.Objects;
import java.util.Random;

import static com.example.healthgenie.base.exception.UserErrorResult.DUPLICATED_NICKNAME;
import static com.example.healthgenie.base.exception.UserErrorResult.USER_NOT_FOUND;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3UploadUtils s3UploadUtils;

    @Transactional
    public UserResponse signUp(String email, String name, AuthProvider authProvider) {
        String defaultNickname = createUniqueNickname();

        User user = User.builder()
                .email(email)
                .name(name)
                .nickname(defaultNickname)
                .authProvider(authProvider)
                .uniName("")
                .role(Role.EMPTY)
                .level(Level.EMPTY)
                .build();

        return UserResponse.of(userRepository.save(user));
    }

    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return UserResponse.of(user);
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
        if(StringUtils.hasText(request.getAuthProvider().getAuthProvider())) {
            user.updateAuthProvider(request.getAuthProvider());
        }
        // 역할
        if(StringUtils.hasText(request.getRole().getCode())) {
            user.updateRole(request.getRole());
        }
        // 프로필 사진
        if(!request.getProfilePhoto().isEmpty()) {
            String profilePhoto = uploadAndDelete(request.getProfilePhoto(), user.getProfilePhoto());
            user.updateProfilePhoto(profilePhoto);
        }
        // 이메일 인증 확인
        if(request.getEmailVerify()) {
            user.updateEmailVerify(true);
        }
        // 단계
        if(StringUtils.hasText(request.getLevel().getCode())) {
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
        if(StringUtils.hasText(request.getGender().getCode())) {
            user.updateGender(request.getGender());
        }

        return UserResponse.of(user);
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
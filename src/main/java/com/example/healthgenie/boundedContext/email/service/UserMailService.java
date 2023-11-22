package com.example.healthgenie.boundedContext.email.service;


import com.example.healthgenie.boundedContext.email.dto.EmailAuthResponseDto;
import com.example.healthgenie.boundedContext.email.entity.EmailAuthCode;
import com.example.healthgenie.boundedContext.email.repository.EmailAuthRepository;
import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserMailService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final EmailAuthRepository emailAuthRepository;

    @Transactional
    public EmailAuthResponseDto sendCode(String toEmail) throws MessagingException {

        String title = "Health Genie 이메일 인증 번호";
        String authCode = this.createCode();

        JsonObject jsonObject = new Gson().fromJson(toEmail, JsonObject.class);
        String email = jsonObject.get("email").getAsString();

        mailService.sendEmail(email, title, authCode);

        // 이메일 인증 요청 시 인증 번호 DTO에 저장
        EmailAuthCode code = EmailAuthCode.builder()
                .code(authCode)
                .email(email)
                .build();

        EmailAuthCode savedCode = emailAuthRepository.save(code);

        return EmailAuthResponseDto.builder().id(savedCode.getId()).build();
    }

    private String createCode() {
        int lenth = 8;
        try {

            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new CommonException(CommonErrorResult.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public boolean verify(String email, String authCode) {
        EmailAuthCode code = getCodeByEmail(email); // email을 이용해 코드 가져오기 (예시 메서드)

        // 코드가 없으면 false 반환하거나 예외처리
        if (code == null) {
            return false;
        }

        // DB에서 가져온 코드와 파라미터로 받은 코드(authCode)를 비교
        return code.getCode().equals(authCode);
    }

    @Transactional(readOnly = true)
    public EmailAuthCode getCodeByEmail(String email) {
        return emailAuthRepository.findByEmail(email).orElseThrow();
    }

}

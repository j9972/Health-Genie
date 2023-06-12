package com.example.healthgenie.service;

import com.example.healthgenie.entity.EmailAuthCode;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.example.healthgenie.repository.EmailAuthCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Setter
@Slf4j
public class EmailService {
    private String authNum; //랜덤 인증 코드
    private final JavaMailSender emailSender;
    private final EmailAuthCodeRepository emailAuthCodeRepository;

    //랜덤 인증 코드 생성
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();

        return authNum;
    }
    public void saveCode(String authCode){
        emailAuthCodeRepository.save(EmailAuthCode.builder().code(authCode).build());
    }

    //코드 검증
    public String valiedCode(String code){
        //코드검증
        Optional<EmailAuthCode> findAuthCode = emailAuthCodeRepository.findByCode(code);
        if(findAuthCode.isEmpty()){
            throw new CommonException(CommonErrorResult.VALID_OUT);
        }
        //검증된 코드 삭제
        emailAuthCodeRepository.deleteById(findAuthCode.get().getId());
        return "true";
        //보완사항

        // 코드 생성시 시간을 설정하고 검증받을때도 현재시간을 사용해 검증의 유효기간을 둔다 20~30분
    }
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("jh485200@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        log.info("send simple message - {}", message);

        emailSender.send(message);

    }
}

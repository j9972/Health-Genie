package com.example.healthgenie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional    // 추가
@AutoConfigureMockMvc
@SpringBootTest
public class EmailServiceTest {

    /*
    test code 작성시, setUp 메소드 필수
 */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
//        userService = new UserService(userRepository, passwordEncoder, authenticationManager,
//                emailService, jwtUtil, refreshTokenService);
    }

//    @Autowired
//    JavaMailSenderImpl mailSender;
//    // MimeMessage 객체를 직접 생성하여 메일을 발송하는 방법
//    @Test
//    public void mailSendTest() throws Exception{
//
//
//        String subject = "test 메일";
//        String content = "메일 테스트 내용" + "<img src=\"https://t1.daumcdn.net/cfile/tistory/214DCD42594CC40625\">";
//        String from = "보내는이 아이디@도메인주소";
//        String to = "받는이 아이디@도메인주소";
//
//        try {
//            MimeMessage mail = mailSender.createMimeMessage();
//            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");
//
//            mailHelper.setFrom(from);
//            mailHelper.setTo(to);
//            mailHelper.setSubject(subject);
//            mailHelper.setText(content, true);
//
//            mailSender.send(mail);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    // MimeMessagePreparator를 사용해서 메일을 전송하는 방법
//    @Test
//    public void mailSendTest2() throws Exception{
//
//        String subject = "test 메일";
//        String content = "메일 테스트 내용" + "<img src=\"https://t1.daumcdn.net/cfile/tistory/214DCD42594CC40625\">";
//        String from = "보내는이 아이디@도메인주소";
//        String to = "받는이 아이디@도메인주소";
//
//        try {
//            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
//
//                public void prepare(MimeMessage mimeMessage) throws Exception{
//                    final MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//                    mailHelper.setFrom(from);
//                    mailHelper.setTo(to);
//                    mailHelper.setSubject(subject);
//                    mailHelper.setText(content, true);
//
//                }
//
//            };
//
//            mailSender.send(preparator);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
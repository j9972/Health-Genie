package com.example.healthgenie.boundedContext.email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.healthgenie.base.exception.CommonErrorResult;
import com.example.healthgenie.base.exception.CommonException;
import com.example.healthgenie.base.utils.SecurityUtils;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;



@Service
@Slf4j
@RequiredArgsConstructor
public class UserMailService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;
    private final RedisService redisService;
    private final UserService userService;

    private static final OkHttpClient client = new OkHttpClient();
    private static final JSONParser parser = new JSONParser();


    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional
    public String sendCode(String toEmail) throws MailException {

        String title = "Health Genie 이메일 인증 번호";
        String authCode = this.createCode();

        mailService.sendEmail(toEmail, title, authCode);

        redisService.setValues(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
        return authCode;
    }

    private String createCode() {
        int length = 8;
        try {

            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException(CommonErrorResult.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public boolean verify(String email, String authCode) throws IOException {

        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        boolean verification = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        User user = SecurityUtils.getCurrentUser();
        Long userId = SecurityUtils.getCurrentUserId();

        if (verification) {
            userService.edit(userId, UserRequest.builder().emailVerify(true).build());
            user.updateEmailVerify(true);
        }

        return verification;
    }

    @Transactional
    public Map<String, Object> certify(String key, String email, String universityName) throws IOException{

        log.info("execute service");

        String url = "http://localhost:1234/auth/mail/send";

        log.info("key : {} , email : {}, universityName : {}", key, email, universityName);

        Request.Builder builder = new Request.Builder().url(url).get();

        JSONObject postObj = new JSONObject();
        postObj.put("key", key);
        postObj.put("email", email);
        postObj.put("universityName", universityName);

        log.info("postObj : {}",postObj);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postObj.toJSONString());
        builder.post(requestBody);
        Request request = builder.build();

        log.info("request : {}", request);

        Response responseHTML = client.newCall(request).execute();

        log.info("parseHTMLToJSON(responseHTML); : {}", parseHTMLToJSON(responseHTML));

        return parseHTMLToJSON(responseHTML);
    }

    private static Map<String, Object> parseHTMLToJSON(Response responseHTML) {
        ResponseBody body = responseHTML.body();
        Map map = new HashMap<>();
        try{
            if (body != null) {
                JSONObject response = (JSONObject) parser.parse(body.string());
                response.put("code", responseHTML.code());
                System.out.println(response.toJSONString());
                map = new ObjectMapper().readValue(response.toJSONString(), Map.class) ;
                return map;
            }
        }
        catch(Exception e){
            System.out.println("json 오류");
            return map; /** 오류 시 빈 맵 */
        }
        return map;
    }
}

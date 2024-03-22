package com.example.healthgenie.boundedContext.email.controller;

import com.example.healthgenie.base.exception.Common.CommonException;
import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.email.dto.MailRequestDto;
import com.example.healthgenie.boundedContext.email.service.UniDomainService;
import com.example.healthgenie.boundedContext.email.service.UserMailService;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.univcert.api.UnivCert;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("/mail")
public class MailController {

    private final UserMailService userMailService;
    private final UniDomainService uniDomainService;

    @Value("${univCert.key}")
    private String KEY;

    // 이메일 코드전송, 이메일 유효성검사 -> accessToken 필요
    @PostMapping
    public ResponseEntity<Result> sendUnivCertMail(@RequestBody MailRequestDto dto, @AuthenticationPrincipal User user)
            throws IOException {

        UnivCert.clear(KEY, dto.getUniv_email());

        String uniDomain = uniDomainService.findUniDomain(dto.getUnivName());

        if (uniDomainService.checkDomain(dto.getUniv_email(), uniDomain)) {
            Map<String, Object> result = UnivCert.certify(KEY, dto.getUniv_email(), dto.getUnivName(), false);

            if ((boolean) result.get("success")) {
                userMailService.updateUniv(dto.getUnivName(), user.getId());
            }

            return ResponseEntity.ok(Result.of("이메일이 성공적으로 보내졌습니다."));

        } else {
            log.warn("이메일의 도메인이 해당 학교 도메인과 다릅니다");
            throw CommonException.WRONG_DOMAIN;
        }
    }

    @GetMapping("/verifications")
    public ResponseEntity<Result> validMailCode(@RequestParam(name = "univ_email") String univ_email,
                                                @RequestParam(name = "univName") String univName,
                                                @RequestParam(name = "code") int code,
                                                @AuthenticationPrincipal User user) throws IOException {

        Map<String, Object> response = UnivCert.certifyCode(KEY, univ_email, univName, code);

        boolean success = (boolean) response.get("success");

        if (success) {
            userMailService.updateUnivVerify(user.getId());
        }

        return ResponseEntity.ok(Result.of(success ? "검증이 성공했습니다" : "검증이 실패했습니다"));

    }
}

package com.example.healthgenie.base.utils.CustomValidator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

/**
 * 공통 유효성 검증 유틸
 * 메서드 명 통일(오버 로딩 사용) -> isValid(...)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonValidator {

    public static boolean isValid(String date) {
        try {
            if(!StringUtils.hasText(date)) {
                return false;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

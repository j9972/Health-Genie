package com.example.healthgenie.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class BasicUtils {

    private BasicUtils() {

    }

    /*
        ResponseEntity
        - 사용자의 HttpRequest 에 대한 응답 데이터를 포함하는 클래스이다. 따라서 HttpStatus, HttpHeaders, HttpBody 포함
     */
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage +"\"}", httpStatus);
    }

}

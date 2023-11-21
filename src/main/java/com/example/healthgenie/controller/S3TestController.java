package com.example.healthgenie.controller;

import com.example.healthgenie.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3TestController {

    private final S3UploadService s3UploadService;

    @PostMapping("/images")
    public String test(@RequestParam("image") MultipartFile file) throws IOException {
        String savedUrl = s3UploadService.upload(file, "test");
        log.info("savedUrl={}", savedUrl);

        return "image upload success!";
    }
}

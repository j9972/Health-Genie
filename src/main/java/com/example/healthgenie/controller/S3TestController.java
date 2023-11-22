package com.example.healthgenie.controller;

import com.example.healthgenie.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3TestController {

    private final S3UploadService s3UploadService;

    @PostMapping("/image")
    public String test(@RequestParam("image") MultipartFile file) throws IOException {
        String savedUrl = s3UploadService.upload(file, "test");
        log.info("savedUrl={}", savedUrl);

        return "file upload success!";
    }

    @PostMapping("/images")
    public String test(@RequestParam("images")List<MultipartFile> files) throws IOException {
        List<String> savedUrls = s3UploadService.upload(files, "test");
        log.info("savedUrls={}", savedUrls);

        return "files upload success!";
    }
}

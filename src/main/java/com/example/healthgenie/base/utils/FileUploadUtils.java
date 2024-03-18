package com.example.healthgenie.base.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class FileUploadUtils {
    //  이미지 경로는 추후에 변경
    private static final String UPLOAD_DIR = "/Users/jungsuyoung/healthgenie/images";

    public static String saveFileAndGetUrl(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }

        String filename = file.getOriginalFilename();

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath);

        return filePath.toString();
    }
}


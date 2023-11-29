package com.example.healthgenie.base.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3UploadUtils {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * @return 저장된 파일의 경로
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));

        return upload(uploadFile, dirName);
    }

    /**
     * @return 저장된 파일들의 경로
     */
    public List<String> upload(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> paths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            paths.add(upload(multipartFile, dirName));
        }

        return paths;
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName(); // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);

        return uploadImageUrl;
    }
    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();

    }

    /**
     * S3에서 파일 삭제
     *
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void deleteS3Object(String dirName, String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        try {
            amazonS3Client.deleteObject(bucket, dirName + "/" + fileName);
            log.info("File delete from S3 success");
        } catch (AmazonServiceException e) {
            log.error("Error occurred while deleting file from S3", e);
        }
    }

    // S3 URL에서 파일 이름 추출
    private String extractFileNameFromUrl(String fileUrl) {
        String urlEncodedFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        return URLDecoder.decode(urlEncodedFileName, StandardCharsets.UTF_8);
    }
}

package com.visiontarot.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
public class S3Service {
    private final S3Client s3Client;
    private final String bucketName;
    private final String concernCardFolder;

    public S3Service(S3Client s3Client,
                     @Value("${aws.s3.bucket-name}") String bucketName,
                     @Value("${aws.s3.concern-card-folder}") String concernCardFolder) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.concernCardFolder = concernCardFolder;
    }


    public String uploadConcernImage(String fileName, byte[] imageBytes) throws IOException {
        log.info("이미지 크기: {} bytes", imageBytes.length);
        String key = concernCardFolder + fileName + ".jpg";
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(imageBytes));
        String url = "https://" + bucketName + ".s3." + Region.AP_NORTHEAST_2 + ".amazonaws.com/" + key;
        log.info(">>> 업로드를 시작합니다. url: {}", url);
        return url;
    }
}

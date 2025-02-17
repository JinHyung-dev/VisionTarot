package com.visiontarot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
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


    public void uploadConcernImage(String fileName, byte[] imageBytes) {
        log.info("이미지 크기: {} bytes", imageBytes.length);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(concernCardFolder + fileName + ".png")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(imageBytes));
    }
}

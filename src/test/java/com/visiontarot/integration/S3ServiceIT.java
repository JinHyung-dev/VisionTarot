package com.visiontarot.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.visiontarot.service.S3Service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@SpringBootTest
@Slf4j
public class S3ServiceIT {
    @Autowired
    private S3Service s3Service;

    private static final String REGION = "ap-northeast-2";

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.concern-card-folder}")
    private String concernCardFolder;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Test
    public void uploadFile() throws IOException, URISyntaxException {
        Path filePath = Paths.get(getClass().getClassLoader().getResource("test-file.jpg").toURI());
        byte[] imageBytes = Files.readAllBytes(filePath);
        String fileName = "test-file";
        String key = concernCardFolder + fileName + ".jpg";
        String imageUrl = s3Service.uploadConcernImage(fileName, imageBytes);

        assertNotNull(imageUrl);
        log.debug(">>> 업로드 성공 url : {}", imageUrl);

        S3Client s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.headObject(headObjectRequest);
            log.info(">>> 테스트 성공(업로드 성공) : {}", key);
        } catch (NoSuchKeyException e) {
            fail(">>> 테스트 실패(업로드 실패) : " + key);
            log.debug(e.getMessage());
        }

        // 테스트 후 S3에서 파일 삭제
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}

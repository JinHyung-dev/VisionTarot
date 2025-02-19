package com.visiontarot.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {
    private S3Service s3Service;
    private String bucketName = "test-bucket";
    private String concernCardFolder = "test-concern-card";

    @Captor
    private ArgumentCaptor<PutObjectRequest> requestCaptor;

    @Mock
    S3Client mockS3Client;

    @BeforeEach
    public void setUp() {
        s3Service = new S3Service(mockS3Client, bucketName, concernCardFolder);
    }

    @Test
    public void 고민카드업로드_putObject수행_업로드경로리턴() throws IOException {
        byte[] testImageBytes = "testImage".getBytes();
        String fileName = "testFileName";
        String key = concernCardFolder + fileName + ".png";

        s3Service.uploadConcernImage(fileName, testImageBytes);

        verify(mockS3Client).putObject(requestCaptor.capture(), any(RequestBody.class));

        PutObjectRequest putObjectRequest = requestCaptor.getValue();
        Assertions.assertThat(putObjectRequest.bucket()).isEqualTo(bucketName);
        Assertions.assertThat(putObjectRequest.key()).isEqualTo(key);
    }
}

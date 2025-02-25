package com.visiontarot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.visiontarot.service.ConcernCardService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
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

@Slf4j
@SpringBootTest
public class ConcernCardServiceIT {
    @Autowired
    private ConcernCardService concernCardService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Test
    public void 고민카드생성_BufferedImage리턴() {
        String sampleGeminiAnswer = "King of Wands 역방향이 나왔다면, 긍정적인 면과 부정적인 면 모두를 고려해야 합니다.";

        BufferedImage result = concernCardService.createImage(sampleGeminiAnswer);

        assertNotNull(result);
        assertEquals(401, result.getWidth());
        assertEquals(590, result.getHeight());
    }

    @Test
    public void 생성된고민카드업로드_url리턴() {
        String sampleGeminiAnswer = "King of Wands 역방향이 나왔다면, 긍정적인 면과 부정적인 면 모두를 고려해야 합니다.";

        BufferedImage result = concernCardService.createImage(sampleGeminiAnswer);
        String uploadUrl = concernCardService.uploadImageToS3(result);

        assertNotNull(uploadUrl);
        log.debug(">>> 생성된 고민카드 이미지 주소 : {}", uploadUrl);
        BufferedImage concernCard = null;
        try {
            concernCard = ImageIO.read(new URL(uploadUrl));
            if (concernCard == null) {
                throw new IOException("이미지를 불러올 수 없음");
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 다운로드 및 크기 확인 실패", e);
        }
        assertEquals(401, concernCard.getWidth());
        assertEquals(590, concernCard.getHeight());

        // 테스트 후 S3에서 파일 삭제
        log.info(">>> 테스트를 통과하여 업로드된 파일을 다시 삭제합니다.");
        S3Client s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(S3key추출(uploadUrl))
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String S3key추출(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return url.getPath().substring(1);
        } catch (Exception e) {
            throw new RuntimeException("S3 객체 키 추출 실패", e);
        }
    }
}

package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.visiontarot.config.FontConfiguration;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ConcernCardServiceTest {
    private ConcernCardService concernCardService;
    private final String IMAGE_TEMPLATE_PATH = "static/images/concernCard/background.jpg";

    @Mock
    private FontConfiguration fontConfig;

    @Mock
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        concernCardService = new ConcernCardService(fontConfig, s3Service);
    }

    @Test
    void 고민카드생성_템플릿불러오기_내용작성() {
        String testText = "테스트";
        
        BufferedImage resultImage = concernCardService.createImage(testText);

        assertNotNull(resultImage);
        assertEquals(401, resultImage.getWidth());
        assertEquals(590, resultImage.getHeight());
    }
}

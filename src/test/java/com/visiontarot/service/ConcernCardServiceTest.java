package com.visiontarot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.visiontarot.config.FontConfiguration;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConcernCardServiceTest {
    private ConcernCardService concernCardService;
    @Autowired
    private FontConfiguration fontConfig;
    private final String IMAGE_TEMPLATE_PATH = "static/images/concernCard/background.jpg";

    @BeforeEach
    void setUp() {
        concernCardService = new ConcernCardService(fontConfig);
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

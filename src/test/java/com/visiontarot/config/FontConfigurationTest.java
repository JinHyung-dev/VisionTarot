package com.visiontarot.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Font;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FontConfigurationTest {

    @Test
    void 커스텀폰트_불러오기() {
        FontConfiguration fontConfiguration = new FontConfiguration();
        Font font = fontConfiguration.getCustomFont(30f);

        assertThat(font).isNotNull();
        assertThat(font.getSize()).isEqualTo(30);
        assertThat(font.getName()).isEqualTo("온글잎 박다현체 Regular");
    }

    @Test
    void 폰트불러오기실패시_기본폰트적용() {
        FontConfiguration faultyFontConfig = new FontConfiguration() {
            @Override
            public Font getCustomFont(float size) {
                try {
                    throw new IOException("폰트 파일을 찾을 수 없습니다: ");
                } catch (IOException e) {
                    System.err.println("폰트 로드에 실패하여 기본 폰트로 대체합니다. → " + e.getMessage());
                    return new Font("SansSerif", Font.PLAIN, 30); // fallback 기본 폰트
                }
            }
        };

        Font font = faultyFontConfig.getCustomFont(30f);

        assertThat(font.getName()).isEqualTo("SansSerif");
    }
}

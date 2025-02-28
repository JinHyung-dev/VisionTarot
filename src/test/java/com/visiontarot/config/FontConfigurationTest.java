package com.visiontarot.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FontConfigurationTest {

    @Test
    void 커스텀폰트_불러오기() {
        FontConfiguration fontConfiguration = new FontConfiguration();
        List<String> sampleText = List.of("예시 글1", "예시 글2");
        Graphics2D g2d = mock(Graphics2D.class);

        when(g2d.getFontMetrics()).thenReturn(mock(FontMetrics.class));
        Font font = fontConfiguration.getCustomFont(sampleText, g2d);

        assertThat(font).isNotNull();
        assertThat(font.getName()).isEqualTo("온글잎 박다현체 Regular");
    }

    @Test
    void 폰트불러오기실패시_기본폰트적용() {
        FontConfiguration faultyFontConfig = new FontConfiguration() {
            @Override
            public Font getCustomFont(List<String> lines, Graphics2D g2d) {
                try {
                    throw new IOException("폰트 파일을 찾을 수 없습니다: ");
                } catch (IOException e) {
                    System.err.println("폰트 로드에 실패하여 기본 폰트로 대체합니다. → " + e.getMessage());
                    return new Font("SansSerif", Font.PLAIN, 30); // fallback 기본 폰트
                }
            }
        };
        List<String> sampleText = List.of("예시 글1", "예시 글2");
        Graphics2D g2d = mock(Graphics2D.class);
        Font font = faultyFontConfig.getCustomFont(sampleText, g2d);

        assertThat(font.getName()).isEqualTo("SansSerif");
    }
}

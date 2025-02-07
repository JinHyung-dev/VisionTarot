package com.visiontarot.config;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FontConfiguration {
    private static final String FONT_PATH = "/font/Ownglyph_PDH.ttf";

    public Font getCustomFont(float size) {
        try {
            InputStream fontStream = getClass().getResourceAsStream(FONT_PATH);
            if (fontStream == null) {
                throw new IOException("폰트 파일을 찾을 수 없습니다: " + FONT_PATH);
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            System.err.println("폰트 로드에 실패하여 기본 폰트로 대체합니다. → " + e.getMessage());
            return new Font("SansSerif", Font.PLAIN, 30); // fallback 기본 폰트
        }
    }
}

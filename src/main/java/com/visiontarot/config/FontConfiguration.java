package com.visiontarot.config;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FontConfiguration {
    private static final String FONT_PATH = "/font/Ownglyph_PDH.ttf";
    @Value("${concerncard.textbox.width}")
    private int textboxWidth;

    @Value("${concerncard.textbox.height}")
    private int textboxHeight;

    public Font getCustomFont(List<String> lines, Graphics2D g2d) {
        float fontSize = adjustFontToFit(lines, g2d);
        try {
            InputStream fontStream = getClass().getResourceAsStream(FONT_PATH);
            if (fontStream == null) {
                throw new IOException("폰트 파일을 찾을 수 없습니다: " + FONT_PATH);
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            System.err.println("폰트 로드에 실패하여 기본 폰트로 대체합니다. → " + e.getMessage());
            return new Font("SansSerif", Font.PLAIN, 30); // fallback 기본 폰트
        }
    }

    private float adjustFontToFit(List<String> lines, Graphics2D g2d) {
        float fontSize = 20f; // 기본 폰트 크기

        while (true) {
            int totalHeight = lines.size() * g2d.getFontMetrics().getHeight();

            if (totalHeight <= textboxHeight || fontSize <= 16f) { // 최소 크기까지만 줄임
//            if (totalHeight <= textboxHeight) {
                break;
            }
            log.info(">>> 내용이 영역을 벗어나 폰트 사이즈를 줄입니다.");
            fontSize -= 2;
        }

        log.info(">>> 폰트 사이즈 조절 완료 : {}", fontSize);
        return fontSize;
    }

}

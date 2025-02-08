package com.visiontarot.service;

import com.visiontarot.config.FontConfiguration;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ConcernCardService {
    private final FontConfiguration fontConfig;
    private final String IMAGE_TEMPLATE_PATH = "static/images/concernCard/background.jpg";
    private static final int TEXT_BOX_X = 80;  // 좌측 상단 X
    private static final int TEXT_BOX_Y = 162; // 좌측 상단 Y
    private static final int TEXT_BOX_WIDTH = 320;  // 영역 너비
    private static final int TEXT_BOX_HEIGHT = 427; // 영역 높이

    @Autowired
    public ConcernCardService(FontConfiguration fontConfig) {
        this.fontConfig = fontConfig;
    }

    public BufferedImage createImage(String text) {
        if (text == null || text.isBlank()) {
            throw new NullPointerException("텍스트 내용이 비어 있습니다.");
        }

        try{
            BufferedImage image = loadTemplateImage();
            image = writeImageWithFont(text, image); // 내용 작성

            return image;
        } catch (Exception e) {
            throw new RuntimeException("고민카드 이미지 생성 중 오류 발생", e);
        }
    }

    private BufferedImage loadTemplateImage() throws IOException {
        try (InputStream inputStream = new ClassPathResource(IMAGE_TEMPLATE_PATH).getInputStream()) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IOException("고민카드 템플릿 이미지 로드 실패", e);
        }
    }

    public BufferedImage writeImageWithFont(String text, BufferedImage image) {
        Graphics2D g2d = image.createGraphics();
        try {
            // 안티앨리어싱 및 폰트 설정
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font customFont = fontConfig.getCustomFont(30f);
            g2d.setFont(customFont);
            g2d.setColor(Color.BLACK);

            Point textPosition = calculateTextPosition(text, customFont, g2d.getFontRenderContext());
            g2d.drawString(text, textPosition.x, textPosition.y);
        } finally {
            g2d.dispose();
        }
        return image;
    }

    private Point calculateTextPosition(String text, Font font, FontRenderContext frc) {
        Rectangle2D textBounds = font.getStringBounds(text, frc);
        int textWidth = (int) textBounds.getWidth();
        int textHeight = (int) textBounds.getHeight();

        int textX = TEXT_BOX_X + (TEXT_BOX_WIDTH - textWidth) / 2;
        int textY = TEXT_BOX_Y + (TEXT_BOX_HEIGHT - textHeight) / 2 + textHeight;

        return new Point(textX, textY);
    }
}

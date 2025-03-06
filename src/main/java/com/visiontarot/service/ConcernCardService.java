package com.visiontarot.service;

import com.visiontarot.config.FontConfiguration;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConcernCardService {
    private final FontConfiguration fontConfig;
    private final S3Service s3Service;

    @Value("${image.concern-card-template}")
    private String IMAGE_TEMPLATE_PATH;

    @Value("${concerncard.textbox.x}")
    private int textboxX;

    @Value("${concerncard.textbox.y}")
    private int textboxY;

    @Value("${concerncard.textbox.width}")
    private int textboxWidth;

    @Value("${concerncard.textbox.height}")
    private int textboxHeight;

    public ConcernCardService(FontConfiguration fontConfig, S3Service s3Service) {
        this.fontConfig = fontConfig;
        this.s3Service = s3Service;
    }

    public String uploadImageToS3(BufferedImage image) {
        String fileName = String.valueOf(UUID.randomUUID());
        byte[] bytes = null;
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            bytes = baos.toByteArray();

            return s3Service.uploadConcernImage(fileName, bytes);
        } catch (IOException e) {
            log.error("[ERROR] 고민카드 S3 업로드 실패: {}", e.getMessage());
            log.info("고민카드 S3 업로드를 재시도 합니다.");
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(1000);
                    return s3Service.uploadConcernImage(fileName, bytes);
                } catch (IOException | InterruptedException retryEx) {
                    log.error("[ERROR]고민카드 S3 재시도 실패: {} ({}번째 시도)", retryEx.getMessage(), i + 1);
                }
            }
        }
        //saveToLocalDatabase(fileName, imageBytes);
        return null;
    }

    public BufferedImage createImage(String text) {
        if (text == null || text.isBlank()) {
            throw new NullPointerException("텍스트 내용이 비어 있습니다.");
        }

        try{
            log.info(">>> 고민카드 이미지 생성을 시작합니다.");
            BufferedImage image = loadTemplateImage();
            image = writeImageWithFont(text, image); // 내용 작성

            return image;
        } catch (Exception e) {
            throw new RuntimeException("고민카드 이미지 생성 중 오류 발생", e);
        }
    }

    public BufferedImage loadTemplateImage() throws IOException {
        try (InputStream inputStream = new ClassPathResource(IMAGE_TEMPLATE_PATH).getInputStream()) {
            log.info(">>> 고민카드 템플릿 이미지 로딩중...");
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IOException("고민카드 템플릿 이미지 로드 실패", e);
        }
    }

    public BufferedImage writeImageWithFont(String text, BufferedImage image) {
        log.info(">>> 고민카드 내용 작성을 시작합니다.");
        Graphics2D g2d = image.createGraphics();
        try {
            log.info(">>> 안티앨리어싱 및 폰트 설정중..");
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 텍스트 나누기
            List<String> lines = wrapTextToBox(text, g2d, textboxWidth);

            Font customFont = fontConfig.getCustomFont(lines, g2d);
            g2d.setFont(customFont);
            g2d.setColor(Color.BLACK);

            // 중앙 정렬
            int totalTextHeight = lines.size() * g2d.getFontMetrics().getHeight();
            int startY = textboxY + (textboxHeight - totalTextHeight) / 2;

            for (String line : lines) {
                log.info("{}", line);
                int textWidth = g2d.getFontMetrics().stringWidth(line);
                int startX = textboxX + (textboxWidth - textWidth) / 2;
                g2d.drawString(line, startX, startY);
                startY += g2d.getFontMetrics().getHeight();
            }
        } finally {
            g2d.dispose();
        }
        return image;
    }

    public List<String> wrapTextToBox(String text, Graphics2D g2d, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        log.info(">>> 고민카드에 들어갈 내용 문장 길이를 조정하는 중...(최대길이 : {})", maxWidth);
        for (String word : text.split(" ")) {
            if (g2d.getFontMetrics().stringWidth(line + word) > maxWidth) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            if (!line.isEmpty()) {
                line.append(" ");
            }
            line.append(word);
        }
        lines.add(line.toString()); // 마지막 줄 추가

        log.info(">>> 조정 결과 : {}, 사이즈 : {}", lines.toString(), lines.size());
        return lines;
    }
}

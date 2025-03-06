package com.visiontarot.api;

import com.visiontarot.dto.CardResponseDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.service.ConcernCardService;
import com.visiontarot.service.GeminiService;
import java.awt.image.BufferedImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/concern-card")
@Slf4j
public class ConcernCardController {
    private final ConcernCardService concernCardService;
    private final GeminiService geminiService;

    @Autowired
    public ConcernCardController(ConcernCardService concernCardService, GeminiService geminiService) {
        this.concernCardService = concernCardService;
        this.geminiService = geminiService;
    }

    @PostMapping("/create")
    public ResponseEntity<CardResponseDTO> createConcernCard (
            @RequestBody String prevGeminiAnswer) {
        log.info(">> /concern-card/create 요청 수신({})", prevGeminiAnswer);
        GeminiResponseDTO geminiResponse = geminiService.getGeminiSummary(prevGeminiAnswer);
        String newSummary = geminiResponse.getGeminiAnswer();
        BufferedImage concernCard = concernCardService.createImage(newSummary);
        String concernCardImgUrl = concernCardService.uploadImageToS3(concernCard);

        return ResponseEntity.ok(new CardResponseDTO(null, null, newSummary, concernCardImgUrl));
    }
}

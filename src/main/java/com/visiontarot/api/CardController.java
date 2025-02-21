package com.visiontarot.api;

import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.CardResponseDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.service.CardService;
import com.visiontarot.service.ConcernCardService;
import java.awt.image.BufferedImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/card")
@Slf4j
public class CardController {
    private final CardService service;
    private final ConcernCardService concernCardService;

    @Autowired
    public CardController(CardService service, ConcernCardService concernCardService) {
        this.service = service;
        this.concernCardService = concernCardService;
    }

    @GetMapping("/onecard")
    public String onecard(){
        return "card";
    }

    @GetMapping("/onecard/draw")
    public ResponseEntity<CardDTO> drawOneCard() {
        return ResponseEntity.ok(service.drawOneCard());
    }

    @PostMapping("/onecard/draw-with-concerncard")
    public ResponseEntity<CardResponseDTO> drawOneCard(@RequestBody String concern) {
        CardDTO card = service.drawOneCard();

        GeminiResponseDTO geminiResponse = service.getGeminiResponse(concern, card);
        String geminiAnswer = geminiResponse.getGeminiAnswer();

        BufferedImage concernCard = concernCardService.createImage(geminiAnswer);
        String concernCardImgUrl = concernCardService.uploadImageToS3(concernCard);

        return ResponseEntity.ok(new CardResponseDTO(card, concern, geminiAnswer, concernCardImgUrl));
    }

    @GetMapping("/threecard")
    public String threecard() {
        return "card";
    }

}

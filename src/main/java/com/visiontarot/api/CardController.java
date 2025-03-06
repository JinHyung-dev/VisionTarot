package com.visiontarot.api;

import com.visiontarot.dto.CardDTO;
import com.visiontarot.dto.CardResponseDTO;
import com.visiontarot.dto.GeminiResponseDTO;
import com.visiontarot.service.CardService;
import com.visiontarot.service.GeminiService;
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
    private final GeminiService geminiService;

    @Autowired
    public CardController(CardService service, GeminiService geminiService) {
        this.service = service;
        this.geminiService = geminiService;
    }

    @GetMapping("/onecard")
    public String onecard(){
        return "card";
    }

    @GetMapping("/onecard/draw")
    public ResponseEntity<CardDTO> drawOneCard() {
        return ResponseEntity.ok(service.drawOneCard());
    }

    @PostMapping("/onecard/draw-with-analyze")
    public ResponseEntity<CardResponseDTO> drawOneCard(@RequestBody String concern) {
        log.info("/card/onecard/draw-with-analyze: 요청 수신({})", concern);
        CardDTO card = service.drawOneCard();
        GeminiResponseDTO geminiResponse = geminiService.getGeminiAnalyze(concern, card);
        String geminiAnswer = geminiResponse.getGeminiAnswer();

        return ResponseEntity.ok(new CardResponseDTO(card, concern, geminiAnswer, null));
    }

    @GetMapping("/threecard")
    public String threecard() {
        return "card";
    }

}

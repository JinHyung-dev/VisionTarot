package com.visiontarot.card.controller;

import com.visiontarot.card.model.CardDTO;
import com.visiontarot.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardDrawController {
    private final CardService service;

    @Autowired
    public CardDrawController(CardService service) {
        this.service = service;
    }

    @GetMapping("/onecard/draw")
    public CardDTO drawOneCard() {
        return service.drawOneCard();
    }
}

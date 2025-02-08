package com.visiontarot.api;

import com.visiontarot.domain.CardDTO;
import com.visiontarot.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/card")
public class CardController {
    private final CardService service;

    @Autowired
    public CardController(CardService service) {
        this.service = service;
    }

    @GetMapping("/onecard")
    public String onecard(){
        return "card";
    }

    @GetMapping("/onecard/draw")
    @ResponseBody
    public CardDTO drawOneCard() {
        return service.drawOneCard();
    }

    @GetMapping
    @ResponseBody
    public

    @GetMapping("/threecard")
    public String threecard() {
        return "card";
    }

}

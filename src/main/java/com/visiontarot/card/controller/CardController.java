package com.visiontarot.card.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/card")
public class CardController {
    public CardController() {}

    @GetMapping("/onecard")
    public String onecard(){
        return "card";
    }

    @GetMapping("/threecard")
    public String threecard() {
        return "card";
    }

}

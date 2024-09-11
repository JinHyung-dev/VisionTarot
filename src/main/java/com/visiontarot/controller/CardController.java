package com.visiontarot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardController {

    @GetMapping("hello")
    public String index(Model model){
        model.addAttribute("data", "guest");
        return "hello";
    }
}

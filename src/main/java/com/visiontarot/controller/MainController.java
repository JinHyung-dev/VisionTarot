package com.visiontarot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("data", "guest");
        return "index";
    }
    @GetMapping("/card/onecard")
    public String onecard(Model model){
        return "card";
    }
    @GetMapping("/card/threecard")
    public String threecard(Model model) {
        return "threecard";
    }
}

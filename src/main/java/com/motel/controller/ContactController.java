package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ContactController {
    
    @GetMapping("/contact")
    public String ShowContact() {
        return "home/Contacts";
    }
    
}

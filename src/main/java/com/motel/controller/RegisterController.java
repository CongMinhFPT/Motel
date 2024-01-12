package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegisterController {
    
    @GetMapping("/signin")
    public String Sigin() {
        return "home/signin";
    }
    
}

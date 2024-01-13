package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegisterController {
    
    @GetMapping("/signin")
    public String Signin() {
        return "home/signin";
    }
    
    @GetMapping("/signup")
    public String Signup() {
        return "home/signup";
    }

    @GetMapping("/change")
    public String Change() {
        return "home/change_password";
    }

    @GetMapping("/forgot")
    public String Forgot() {
        return "home/forgot_password";
    }

    @GetMapping("/information")
    public String Information() {
        return "home/information";
    }
}

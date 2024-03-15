package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/index")
    public String getMethodName() {
        return "home/index";
    }
    @GetMapping("/news")
    public String News(){
        return "home/news";
    }
    @GetMapping("/news_details")
    public String News_details(){
        return "home/news_details";
    }
}

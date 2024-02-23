package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogController {

    @GetMapping("/blog")
    public String Blog() {
        return "home/blog";
    }

    @GetMapping("/blog_details")
    public String Blog_details() {
        return "home/blog_details";
    }
}

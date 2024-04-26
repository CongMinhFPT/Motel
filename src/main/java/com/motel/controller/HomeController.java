package com.motel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.entity.Blog;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.entity.Tag;
import com.motel.service.BlogService;
import com.motel.service.MotelRoomService;
import com.motel.service.PostService;
import com.motel.service.RenterService;
import com.motel.service.impl.ManageMotelImpl;


@Controller
public class HomeController {
    @Autowired
    ManageMotelImpl impl;
    
	@Autowired private BlogService blogService;
    @Autowired private PostService postService;
    @Autowired private RenterService renterService;
    
    @GetMapping("/index")
    public String getMethodName(Model model) {
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

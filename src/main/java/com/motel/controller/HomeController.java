package com.motel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.entity.CustomUserDetails;
import com.motel.service.impl.ManageMotelImpl;


@Controller
public class HomeController {
    @Autowired
    ManageMotelImpl impl;
    @GetMapping("/index")
    public String getMethodName() {
        if (impl.checklogin().isPresent()) {
            CustomUserDetails access =impl.checklogin().get();
            System.out.println(access.getAuthorities()+"Loi");
            System.out.println(access.getUsername()+"Loi");
            return "home/index";
        }
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
    
    @GetMapping("/room-detail")
    public String showRoomDetal(){
        return "room/room_detail";
    }

    
}

package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/index")
    public String getMethodName() {
        return "home/index";
    }
   
    
    @GetMapping("/room-detail")
    public String showRoomDetal(){
        return "room/room_detail";
    }


}


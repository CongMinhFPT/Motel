package com.motel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RoomController {
    @GetMapping("/room")
    public String getMethodName() {
        return "home/room";
    }
    
}

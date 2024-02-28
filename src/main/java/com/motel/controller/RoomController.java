package com.motel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.service.AccountService;


@Controller
public class RoomController {
    @GetMapping("/room")
    public String getMethodName(Authentication authentication, Model model) {
        model.addAttribute("accountId", authentication.getName());
        return "home/room";
    }
    
}

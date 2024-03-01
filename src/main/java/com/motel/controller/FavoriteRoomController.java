package com.motel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.MotelRoom;
import com.motel.service.MotelRoomService;

@Controller
public class FavoriteRoomController {

    @Autowired
    MotelRoomService motelRoomService;

    @GetMapping("/favorite")
    public String showFavoriteRoom(Model model) {
        return "/home/favoriteroom";
    }

}

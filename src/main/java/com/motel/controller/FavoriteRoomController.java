//package com.motel.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.motel.entity.FavoriteRoom;
//import com.motel.entity.MotelRoom;
//import com.motel.service.FavoriteRoomService;
//import com.motel.service.MotelRoomService;
//
//@Controller
//public class FavoriteRoomController {
//
//    @Autowired
//    MotelRoomService motelRoomService;
//
//    @Autowired
//    FavoriteRoomService favoriteRoomService;
//
//    @GetMapping("/favorite")
//    public String showFavoriteRoom(Model model, Pageable pageable) {
//        Page<FavoriteRoom> roomPage = favoriteRoomService.getAllFavoriteRoom(pageable);
//        model.addAttribute("page", roomPage);
//        return "/home/favoriteroom";
//    }
//
//}

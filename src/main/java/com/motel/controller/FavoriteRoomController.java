package com.motel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;
import com.motel.repository.AccountsRepository;
import com.motel.repository.FavoriteRoomRepository;
import com.motel.service.AccountService;
import com.motel.service.FavoriteRoomService;
import com.motel.service.MotelRoomService;

@Controller
public class FavoriteRoomController {

    @Autowired
    MotelRoomService motelRoomService;

    @Autowired
    FavoriteRoomService favoriteRoomService;
    
    @Autowired
    FavoriteRoomRepository favoriteRoomRepository;
    
    @Autowired
    AccountsRepository accountsRepository;

    @GetMapping("/favorite")
    public String showFavoriteRoom(Model model, Authentication authentication) {
    	String email = authentication.getName();
    	Account account = accountsRepository.getByEmail(email);
    	
    	
        List<FavoriteRoom> favoriteRooms = favoriteRoomRepository.findFavoriteRoomByAccount(account.getAccountId());
        model.addAttribute("favoriteRooms", favoriteRooms);
        return "/home/favoriteroom";
    }

}

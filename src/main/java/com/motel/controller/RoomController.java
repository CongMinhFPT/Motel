package com.motel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.service.MotelRoomService;


@Controller
public class RoomController {

    @Autowired AccountsRepository accountsRepository;
    @Autowired
    MotelRoomService motelRoomService;
    @Autowired MotelRepository motelRepository;

    @GetMapping("/showroom")
    public String getMethodName(Authentication authentication, Model model) {
        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        model.addAttribute("accountId", account.getAccountId());
        return "home/room";
    }
    
}

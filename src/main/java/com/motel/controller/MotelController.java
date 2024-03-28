package com.motel.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



import com.motel.repository.AccountsRepository;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.RoomService;

import DTO.roomDTO;

@Controller
public class MotelController {
	@Autowired
	RoomService roomService;
	@Autowired
	private MotelRepository motelRepository;
	
	@Autowired
	private MotelRepository motelrep;
	@Autowired
	private MotelRoomRepository motelroomrep;
	@Autowired
	private AccountsRepository accountsrep;
	@Autowired
	private CategoryRoomRepository categoryrep;

	@GetMapping("/room")
	public String showRoomDetails(Model model, Pageable pageable) {
	    Page<roomDTO> roomPage = roomService.getAllRoomDTOs(pageable);
	    model.addAttribute("rooms", roomPage.getContent());
	    model.addAttribute("page", roomPage);
	    return "home/room";
	}
	
}

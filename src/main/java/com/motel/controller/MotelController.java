package com.motel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.repository.AccountsRepository;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.RoomService;

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
	public String showRoomDetails(Model model) {
		List<Motel> motels = roomService.findAllMotel();
		List<MotelRoom> motelRooms = roomService.findAllMotelRoom();
		List<Account> accounts = roomService.findAllAccount();
		List<CategoryRoom> category = roomService.findAllCategory();

		model.addAttribute("motels", motels);
		model.addAttribute("motelRooms", motelRooms);
		model.addAttribute("accounts", accounts);
		model.addAttribute("category", category);

		return "home/room";
	}
	
//	@GetMapping("/room")
//	public ResponseEntity<Map<String, Object>> getAuthorities() {
//		Map<String, Object> data = new HashMap<>();
//		data.put("motels", motelrep.findAll());
//        data.put("motelRooms", motelroomrep.findAll());
//        data.put("accounts", accountsrep.findAll());
//        data.put("category", categoryrep.findAll());
//
//		return new ResponseEntity<>(data, HttpStatus.OK);
//	}

}

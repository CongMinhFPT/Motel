package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.entity.CategoryRoom;
import com.motel.service.CategoryRoomService;

@Controller
public class CategoryRoomController {

	@Autowired
	private CategoryRoomService roomService;
	
	@GetMapping("/admin/category-room")
    public String showMotel(Model model){
		
		List<CategoryRoom> listCategoryRooms = roomService.getAll();
		model.addAttribute("listCategoryRooms", listCategoryRooms);
		
		
        return "admin/category/category-list";
    }
}

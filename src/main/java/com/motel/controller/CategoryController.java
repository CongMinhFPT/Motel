package com.motel.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.CategoryRoom;
import com.motel.repository.CategoryRoomRepository;
import com.motel.service.CategoryService;

@Controller
public class CategoryController {
	@Autowired
	CategoryRoomRepository categoryrep;
	@Autowired
	CategoryService categoryService;

	@GetMapping("/admin/category")
	public String categorylist(@ModelAttribute("category") CategoryRoom category, Model model) {
		List<CategoryRoom> category1 = categoryService.findActive();
		model.addAttribute("category", category1);
		return "admin/category/category-list";
	}

	@GetMapping("/admin/categoryform")
	public String categoryform(@ModelAttribute("category") CategoryRoom category, Model model) {
//		model.addAttribute("method", new Method());
		return "admin/category/add-category";
	}

//	@PostMapping("/admin/category/search")
//	public String searchCategory(@RequestParam("keyword") String keyword, Model model) {
//		List<CategoryRoom> searchResult = categoryrep.findByTitle("%" + keyword + "%");
//		model.addAttribute("categoryList", searchResult);
//		return "admin/category/category-list";
//	}

	@PostMapping("/admin/addcategory")
	public String addCategory(@Valid @ModelAttribute("category") CategoryRoom category, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "admin/category/add-category";
		}
		if (categoryrep.getBytitle(category.getTitle()) != null) {
			model.addAttribute("messagerror", "Tên tiêu đề đã tồn tại ! vui lòng sử dụng tên khác");
			return "admin/category/add-category";
		}
		categoryService.create(category);
		model.addAttribute("messageAdd", "Create Success!");
		return "admin/category/add-category";
	}

	@PostMapping("/admin/upcategory")
	public String upcategory(@Valid @ModelAttribute("category") CategoryRoom category, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "admin/category/up-category";
		}
		if (categoryrep.getBytitle(category.getTitle()) != null) {
			model.addAttribute("messagerror", "Tên tiêu đề đã tồn tại ! vui lòng sử dụng tên khác");
			return "admin/category/up-category";
		}
		categoryService.create(category);
		model.addAttribute("messagess", "Create Success!");
		return "admin/category/up-category";
	}

	@GetMapping("/admin/categoryform/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		CategoryRoom category = categoryService.findById(id);
		model.addAttribute("category", category);
		return "admin/category/up-category";
	}

	@GetMapping("/changeStatus/{id}")
	public String changeStatus(@PathVariable("id") int id, Model model) {
		categoryService.changeStatus(id, false);
		model.addAttribute("messageChange", "Change Success!");
		return "redirect:/admin/category";
	}
}

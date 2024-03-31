package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.Tag;
import com.motel.service.TagService;

@Controller
public class AdminTagController {

	@Autowired private TagService tagService;
	@GetMapping("/admin/tags")
	public String allTag(Model model) {
		
		List<Tag> listTags = tagService.getListTag();
		model.addAttribute("listTags", listTags);
		
		return "admin/tag/tag-list";
		
	}
	@GetMapping("/admin/add-tag")
	public String newTag(Model model) {
		Tag tag = new Tag();
		model.addAttribute("tag", tag);
		return "admin/tag/add-tag";
	}
	@GetMapping("/admin/edit-tag/{tagId}")
	public String editTag(@PathVariable(name = "tagId") Integer tagId, Model model, RedirectAttributes ra) {
		try {
			Tag tag = tagService.getId(tagId);
			model.addAttribute("tag", tag);
			return "admin/tag/add-tag";
		} catch (Exception e) {
			// TODO: handle exception
			return "/admin/tags";
		}
	}
	@GetMapping("/admin/remove-tag/{tagId}")
	public String removeTag(@PathVariable(name = "tagId") Integer tagId, RedirectAttributes ra) {
		try {
			tagService.deleteTag(tagId);
			ra.addFlashAttribute("message", "Đã xóa thành công");
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/admin/tags";
	}
	@PostMapping("/admin/save-tag")
	public String saveTag(Tag tag, RedirectAttributes ra) {

		try {
			tagService.save(tag);
			ra.addFlashAttribute("message", "Đã lưu thành công");
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/admin/tags";
	}
}

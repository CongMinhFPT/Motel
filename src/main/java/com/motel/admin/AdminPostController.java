package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.service.MotelRoomService;
import com.motel.service.PostService;

@Controller
public class AdminPostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private MotelRoomService motelRoomService;
	
	
	@GetMapping("/admin/posts")
	public String allPost(Model model) {
		
		List<Post> listPosts = postService.getListPost();
		
		model.addAttribute("listPosts", listPosts);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		System.out.println("Name +==============     : " + name);
		
		return "admin/post/post-list";
		
	}
	
	@GetMapping("/admin/add-post")
	public String newBlog(Model model) {
		Post post = new Post();
		model.addAttribute("post", post);
		
		List<MotelRoom> listMotelRooms = motelRoomService.getAll();
		model.addAttribute("listMotelRooms", listMotelRooms);
		
		return "admin/post/add-post";
	}
	
	@GetMapping("/admin/motelrom-detail/{id}")
	public String detailMotelRoom(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			MotelRoom motelRoom = motelRoomService.getMotelRoomId(id);
			model.addAttribute("motelRoom", motelRoom);
			return "admin/post/motelroom-detail";
		} catch (Exception e) {
			return "redirect:/admin/posts";
		}
		
	}
	
	@PostMapping("/admin/save-post")
	public String saveBlog(Post post, RedirectAttributes ra) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName();
			
			postService.save(post, email);
			
			ra.addFlashAttribute("message", "Đã lưu thành công");
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/admin/posts";
	}
}

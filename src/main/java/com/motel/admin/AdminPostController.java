package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.entity.Blog;
import com.motel.entity.Post;
import com.motel.entity.Tag;
import com.motel.service.PostService;

@Controller
public class AdminPostController {
    
	@Autowired
	private PostService postService;
	
	
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
	public String newPost(Model model) {
		Post post = new Post();
		model.addAttribute("post", post);
		
		
		return "admin/blog/add-blog";
	}
	
}

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

import com.motel.entity.Blog;
import com.motel.entity.Tag;
import com.motel.service.BlogService;
import com.motel.service.TagService;

@Controller
public class AdminBlogController {
	@Autowired
	private BlogService blogService;
	@Autowired 
	private TagService tagService;
	
	@GetMapping("/admin/blogs")
	public String allBlog(Model model) {
		
		List<Blog> listBlogs = blogService.getListBlog();
		
		model.addAttribute("listBlogs", listBlogs);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		System.out.println("Name +==============     : " + name);
		
		return "admin/blog/blog-list";
		
	}
	@GetMapping("/admin/add-blog")
	public String newBlog(Model model) {
		Blog blog = new Blog();
		model.addAttribute("blog", blog);
		
		List<Tag> listTags = tagService.getListTag();
		model.addAttribute("listTags", listTags);
		
		return "admin/blog/add-blog";
	}
	@GetMapping("/admin/edit-blog/{blogId}")
	public String editBlog(@PathVariable(name = "blogId") Integer blogId, Model model) {
		try {
			Blog blog = blogService.getId(blogId);
			List<Tag> listTags = tagService.getListTag();
			
			model.addAttribute("blog", blog);
			model.addAttribute("listTags", listTags);
			
			return "admin/blog/add-blog";
		} catch (Exception ex) {
			// TODO: handle exception
			return "redirect:/admin/blogs";
		}
	}
	@GetMapping("/admin/remove-blog/{blogId}")
	public String removeBlog(@PathVariable(name = "blogId") Integer blogId, RedirectAttributes ra) {
		try {
			blogService.deleteBlog(blogId);
			ra.addFlashAttribute("message", "Đã xóa blog");
		} catch (Exception e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/admin/blogs";
	}
	@PostMapping("/admin/save-blog")
	public String saveBlog(Blog blog, RedirectAttributes ra) {
		blogService.save(blog);
		return "redirect:/admin/blogs";
	}
}

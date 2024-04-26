package com.motel.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.Until.page.PagingAndSortingHelper;
import com.motel.Until.page.PagingAndSortingParam;
import com.motel.entity.Blog;
import com.motel.entity.Tag;
import com.motel.service.BlogService;
import com.motel.service.TagService;

@Controller
public class BlogController {
	
	@Autowired private BlogService blogService;
	@Autowired private TagService tagService;
	
	@GetMapping("/blog")
	public String listFirstPage(Model model) {
		 return "redirect:/blog/page/1?sortField=createDate&sortDir=asc&tagId=0";
	
	}
	
	@GetMapping("/blog/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listBlogs", moduleURL = "/blog") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,@Param("tagId") Integer tagId) {
		
		List<Tag> listTags = tagService.getListTag();
		if(tagId != null) model.addAttribute("tagId", tagId);
		model.addAttribute("listTags", listTags);
		
		blogService.listByPage(pageNum, helper, tagId);
		
		model.addAttribute("moduleURL", "/blog");
		
		return "home/blog";
	}

	@GetMapping("/blog_details/{blogId}")
	public String Blog_details(@PathVariable(name = "blogId") Integer blogId, Model model) {
		Blog blog = blogService.getId(blogId);
		model.addAttribute("blog", blog);
		model.addAttribute("tagName", blog.getTag().getTitle());
		
		List<Blog> blogSimilar = blogService.findBlogSimilar(blog.getTag().getTagId(), blogId);
		model.addAttribute("blogSimilar", blogSimilar);
		
		return "home/blog_details";
	}
}

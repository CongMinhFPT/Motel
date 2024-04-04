package com.motel.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.repository.PostRepository;
import com.motel.service.MotelRoomService;
import com.motel.service.PostService;

@Controller
public class AdminPostController {

	@Autowired
	private PostService postService;

	@Autowired
	private MotelRoomService motelRoomService;

	@Autowired
	PostRepository postRepository;

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

	@GetMapping("/admin/update-post/{id}")
	public String detailMotelRoom(@PathVariable("id") Integer id, Model model, @ModelAttribute("post") Post post) {
		Post posts = postRepository.getById(id);
		model.addAttribute("posts", posts);
		model.addAttribute("postsMotelRoomId", posts.getMotelRoom().getMotelRoomId());
		model.addAttribute("postsMotelRoomDescription", posts.getMotelRoom().getDescriptions());

		List<MotelRoom> listMotelRooms = motelRoomService.getAll();
		model.addAttribute("listMotelRooms", listMotelRooms);	
		return "admin/post/update-post";

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

	@PostMapping("/admin/update-post/{id}")
	public String saveBlog(@PathVariable("id") Integer postId, @ModelAttribute("post") Post post, Model model) {
		Post posts = postRepository.getById(postId);
		model.addAttribute("posts", posts);
		model.addAttribute("postsMotelRoomId", posts.getMotelRoom().getMotelRoomId());
		model.addAttribute("postsMotelRoomDescription", posts.getMotelRoom().getDescriptions());
		System.out.println(posts.getMotelRoom().getDescriptions());

		List<MotelRoom> listMotelRooms = motelRoomService.getAll();
		model.addAttribute("listMotelRooms", listMotelRooms);	
		try {
			
			postService.update(post, postId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/admin/posts";
	}

	@GetMapping("/admin/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId, Model model) {
        postService.deletePost(postId);
        return "redirect:/admin/posts";
    }
}

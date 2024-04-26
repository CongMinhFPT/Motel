package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.motel.entity.Account;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.repository.AccountsRepository;
import com.motel.repository.PostRepository;
import com.motel.service.MotelRoomService;
import com.motel.service.PostService;
import com.motel.service.RenterService;

@Controller
public class AdminOwnerPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MotelRoomService motelRoomService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RenterService renterService;

    @Autowired
    AccountsRepository accountsRepository;

    @GetMapping("/admin/check-posts")
    public String allPost(Model model) {

        List<Post> listPosts = postService.getListPostNonActive();

        model.addAttribute("listPosts", listPosts);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println("Name +==============     : " + name);

        return "admin/post/check-post-list";
    }

    @GetMapping("/admin/check-update-post/{id}")
    public String detailMotelRoom(@PathVariable("id") Integer id, Model model, @ModelAttribute("post") Post post,
            Authentication authentication) {
        Post posts = postRepository.getById(id);
        model.addAttribute("posts", posts);
        // model.addAttribute("postsMotelRoomId",
        // posts.getMotelRoom().getMotelRoomId());
        // model.addAttribute("postsMotelRoomDescription",
        // posts.getMotelRoom().getDescriptions());

        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        // model.addAttribute("accountId", account.getAccountId());

        List<MotelRoom> listMotelRooms = renterService.getMotelRoomByAccount(account.getAccountId());
        model.addAttribute("listMotelRooms", listMotelRooms);
        return "admin/post/check-update-post";

    }

    @PostMapping("/admin/check-update-post/{id}")
    public String saveBlog(@PathVariable("id") Integer postId, @ModelAttribute("post") Post post, Model model,
            Authentication authentication) {
        Post posts = postRepository.getById(postId);
        model.addAttribute("posts", posts);
        // model.addAttribute("postsMotelRoomId",
        // posts.getMotelRoom().getMotelRoomId());
        // model.addAttribute("postsMotelRoomDescription",
        // posts.getMotelRoom().getDescriptions());
        // System.out.println(posts.getMotelRoom().getDescriptions());

        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        // model.addAttribute("accountId", account.getAccountId());

        List<MotelRoom> listMotelRooms = renterService.getMotelRoomByAccount(account.getAccountId());
        model.addAttribute("listMotelRooms", listMotelRooms);
        try {

            postService.checkUpdate(post, postId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "redirect:/admin/check-posts";
    }
}

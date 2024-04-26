package com.motel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.entity.PostMotel;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.PostRepository;
import com.motel.service.AccountService;
import com.motel.service.MotelRoomService;
import com.motel.service.PostService;


@Controller
public class RoomController {

//    @Autowired AccountsRepository accountsRepository;
//    @Autowired
//    MotelRoomService motelRoomService;
//    @Autowired MotelRepository motelRepository;
//
//    @GetMapping("/rooms")
//    public String getMethodName(Authentication authentication, Model model) {
//        String emailAccount = authentication.getName();
//        Account account = accountsRepository.getByEmail(emailAccount);
//        model.addAttribute("accountId", account.getAccountId());
//        return "home/room";
//    }
	
	@Autowired 
	MotelRepository motelRepository;
	
	@Autowired
	PostRepository postRepository;

	@Autowired
	private PostService postService;

	int TAR_GET_COUNT = 3;

	@GetMapping("/post/motel/motelroom/{motelid}")
	public String motelroominmotel(@PathVariable("motelid") Integer motelid ,Model model){
	    Motel motel = motelRepository.getById(motelid);
	    Account account = motel.getAccount();
	    String city = motel.getProvince();
	    List<PostMotel>postMotels = new ArrayList<>();
	     List<Post> posts = postRepository.findPosts(city);
	     int count = 0;
	     for (int i = 0; i < posts.size(); i++) {
	         if (!posts.get(i).getMotel().getMotelId().equals(motel.getMotelId())) {
	             PostMotel motel2 = new PostMotel(posts.get(i));
	             postMotels.add(motel2);
	             count++;
	             if (count == TAR_GET_COUNT) {
	                 break;
	             }
	         }
	     }
	   if (postMotels.size()==0) {
	    model.addAttribute("checkpostnull", false);
	   }else{
	    model.addAttribute("checkpostnull", true);
	    model.addAttribute("listpost", postMotels);
	   }
	    model.addAttribute("motel", motel);
	    model.addAttribute("account", account);
	    return "/home/MotelRoomInMotel";
	}
    
}

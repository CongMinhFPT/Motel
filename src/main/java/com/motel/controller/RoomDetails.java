package com.motel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.motel.entity.Post;
import com.motel.entity.RoomCash;
import com.motel.repository.PostRepository;
import com.motel.repository.RoomCashRepository;

@Controller
public class RoomDetails {
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	RoomCashRepository roomCashRepository;

    @GetMapping("/room-details/{post_id}")
    public String showRoomDetals(@PathVariable("post_id") Integer postId, Model model){
    	Post post = postRepository.findById(postId).orElse(null);
    	if(post != null) {
    		model.addAttribute("roomDetails_hoi", post);
    		 return "room/room_detail";
    	}
		return null;
       
    }
    
	@ModelAttribute("roomDetails")
	public Post roomDetails(@PathVariable("post_id") Integer post_id) {
		Post post = postRepository.findById(post_id).get();
		return post;
	}
	
    @ModelAttribute("roomCash")
    public RoomCash roomCash(@PathVariable("post_id") Integer postId) {
        return roomCashRepository.findByMotelRoom_MotelRoomId(postId);
    }
	
}

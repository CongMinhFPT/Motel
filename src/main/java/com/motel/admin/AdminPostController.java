package com.motel.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.motel.service.PostService;

@Controller
public class AdminPostController {
    
	@Autowired
	private PostService postService;
}

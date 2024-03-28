package com.motel.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.entity.RequestAuthority;
import com.motel.repository.RequestAuthorityRepository;

@Controller
public class AdminController {
    
	@Autowired
    private RequestAuthorityRepository requestAuthority;
	
	@GetMapping("/admin")
    public String index(Model model){
    	int count = requestAuthority.findRequestCount();
    	model.addAttribute("requestCount", count);
        return "admin/home/index";
    }
    @GetMapping("/authority")
    public String authority(Model model) {
    	int count = requestAuthority.findRequestCount();
    	model.addAttribute("requestCount", count);
    	return "admin/authority/auth";
    }
}

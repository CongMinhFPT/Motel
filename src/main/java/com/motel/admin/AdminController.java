package com.motel.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
	@Autowired
    private HttpServletRequest request;
	@GetMapping("/admin")
    public String index(){
    	boolean isSuperManager = request.isUserInRole("STAFF");
    	System.out.println("Is SUPERMANAGER: " + isSuperManager);
        return "admin/home/index";
    }
    @GetMapping("/authority")
    public String authority() {
    	return "admin/authority/auth";
    }
}

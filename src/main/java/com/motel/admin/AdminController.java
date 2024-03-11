package com.motel.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String index(){
        return "admin/home/index";
    }
    @GetMapping("/authority")
    public String authority() {
    	return "admin/authority/auth";
    }
}

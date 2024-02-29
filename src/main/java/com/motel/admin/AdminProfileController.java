package com.motel.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminProfileController {
    @GetMapping("/admin/profile")
    public String showProfile(){
        return "admin/profile/app-profile";
    }
}

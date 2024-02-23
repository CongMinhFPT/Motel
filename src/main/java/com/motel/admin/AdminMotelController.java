package com.motel.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMotelController {
    @GetMapping("/admin/motel")
    public String showMotel(){
        return "admin/motel/motel-list";
    }

    @GetMapping("/admin/add-motel")
    public String showAddMotel(){
        return "admin/motel/add-motel";
    }
}

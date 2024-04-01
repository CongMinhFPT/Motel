package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.entity.Renter;
import com.motel.service.RenterService;

@Controller
public class AdminController {
        @Autowired
    RenterService renterService;
    @GetMapping("/admin")
    public String index(Model model) {
        return "admin/home/index";
    }
}

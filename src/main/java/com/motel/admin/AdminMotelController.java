package com.motel.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.service.impl.ManageMotelImpl;


@Controller
public class AdminMotelController {
    @Autowired
    ManageMotelImpl motelImpl;
    @GetMapping("/admin/motel")
    public String showMotel(){
        return "admin/motel/motel-list";
    }

    @GetMapping("/admin/add-motel")
    public String showAddMotel(){
        return "admin/motel/add-motel";
    }

    @GetMapping("/admin/manage-motel")
    public String getMethodName(HttpServletRequest request , Model model) {
    return motelImpl.checkManageMotel(request, model);
    }
    @GetMapping("/admin/manage-motel/{idmotel}")
    public String getMethodName(HttpServletResponse response ,@PathVariable String idmotel, Model model) {
        int idmotel1 = Integer.valueOf(idmotel);
        System.out.println(idmotel1+"dd");
    return motelImpl.ManageMotelPava(response, idmotel1, model);
    }

    // @GetMapping("/admin/manage-motel/{idmotel}")
    // public String getMethodName1(@PathVariable String idmotel, HttpServletResponse response, Model model) {
    //     int intidmotel = Integer.valueOf(idmotel);
    //    return motelImpl.addcookei(response, intidmotel, model);
    // }
}

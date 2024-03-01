package com.motel.admin;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.Motel;
import com.motel.service.impl.ManageMotelImpl;

@MultipartConfig
@Controller
public class AdminMotelController {
    @Autowired
    ManageMotelImpl motelImpl;
    @GetMapping("/admin/add-motel")
    public String showMotel(@ModelAttribute("motelttr") Motel motel ,Model model){
        return motelImpl.getmotel(model);
    }
    @PostMapping("/admin/add-motel")
    public String postMethodName(@ModelAttribute ("motelttr") @Valid Motel motel ,BindingResult bindingResult  , @RequestParam("files") MultipartFile[] files ,
    Model model ,RedirectAttributes attributes) {
        return motelImpl.addmotel(motel, bindingResult, files, model,attributes);
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

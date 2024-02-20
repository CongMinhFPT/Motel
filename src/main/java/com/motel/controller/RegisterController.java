package com.motel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.repository.AccountsRepository;


@Controller
public class RegisterController {
	
	@Autowired 
	AccountsRepository accountsRepository;
    
    @GetMapping("/signin")
    public String Signin() {
        return "home/signin";

    }
    
    @GetMapping("/signup")
    public String Signup(@ModelAttribute("accounts") Account account) {
        return "home/signup";
    }
    
    @PostMapping("/register/save")
    public String save(@Validated @ModelAttribute("accounts") Account account, BindingResult bindingResult, @RequestParam("password1") String pass, Model model) {
    	if(bindingResult.hasErrors()) {
    		if(pass.equals(account.getPassword())) {
        		model.addAttribute("pass","Xác nhận mật khẩu không đúng!");
        		return "home/signup";
        	}
    		return "home/signup";
    	}
    	
    	accountsRepository.save(account);
    	return "redirect:/signin";
    }
    
    @GetMapping("/change")
    public String Change() {
        return "home/change_password";
    }

    @GetMapping("/forgot")
    public String Forgot() {
        return "home/forgot_password";
    }

    @GetMapping("/information")
    public String Information() {
        return "home/information";
    }
}

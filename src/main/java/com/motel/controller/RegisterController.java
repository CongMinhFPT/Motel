package com.motel.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.Authority;
import com.motel.entity.Role;
import com.motel.repository.AccountsRepository;
import com.motel.repository.AuthorityRepository;
import com.motel.repository.RoleRepository;
import com.motel.service.MailerService;


//@Controller
//public class RegisterController {
//	
//	@Autowired 
//	AccountsRepository accountsRepository;
//    
//	@Autowired
//	RoleRepository roleRepository;
//	
//	@Autowired
//	AuthorityRepository authorityRepository;
//	
//	@Autowired
//	MailerService mailerService;
//	
//	@Autowired
//	HttpSession session;
//	@Autowired
//	BCryptPasswordEncoder pe;
//	
//   
//    
//    @GetMapping("/signup")
//    public String Signup(@ModelAttribute("accounts") Account account) {
//        return "home/signup";
//    }
//    
//    @PostMapping("/register/save")
//    public String save(@Validated @ModelAttribute("accounts") Account account, BindingResult bindingResult, @RequestParam("password1") String pass, Model model) {
//    	if(bindingResult.hasErrors()) {
//    		if(pass.isEmpty()) {
//    			model.addAttribute("pass","Vui lòng nhập xác nhận mật khẩu!");
//    		}
//    		return "home/signup";
//    	}
//    	if(!account.getPassword().equals(pass)) {
//    		model.addAttribute("pass","Xác nhận mật khẩu không đúng!");
//    		return "home/signup";
//    	}
//    	if(accountsRepository.getByEmail(account.getEmail()) != null) {
//    		model.addAttribute("email", "Email này đã tồn tại!");
//    		return "home/signup";
//    	}
//    	
//    	String passw = account.getPassword();
//    	System.out.println("passw>> " + passw);
//    	String passpe = pe.encode(passw);
//    	System.out.println("PassPE>> " + passpe);
//    	account.setPassword(passpe);
//    	accountsRepository.save(account);
//    	
//    	Role staff = roleRepository.findById("STAFF").orElseGet(() -> {
//    		Role newRole = new Role();
//    		newRole.setId("STAFF");
//    		return roleRepository.save(newRole);
//    	});
//    	
//    	Authority au = new Authority();
//    	au.setAccount(account);
//    	au.setRole(staff);
//    	authorityRepository.save(au);
//    	
//    	mailerService.add(memeMessage -> {
//    		MimeMessageHelper helper = new MimeMessageHelper(memeMessage);
//    		try {
//				helper.setTo(account.getEmail());
//				helper.setSubject("Nhà Trọ F.E Xin Chào!");
//				helper.setText("Nhà trọ F.E luôn là lựa chọn tốt nhất. Hân hạnh được phục vụ quí khách!");
//			} catch (MessagingException e) {
//				e.printStackTrace();
//			}
//    	});
//    	
//    	model.addAttribute("create", "Thêm mới thành công!");
//    
//    	return "redirect:/signin";
//    }
//    
//    @RequestMapping("/signin")
//    public String error(Model model, @RequestParam(required = false) String error) {
//    	if (error != null) {
//			model.addAttribute("error", "Đăng nhập thất bại!");
//		}
//        return "home/signin";
//
//    }
//    
//    @RequestMapping("/auth/access/denied")
//    public String denied(Model model) {
//    	model.addAttribute("error", "Bạn không có quyền truy cập!");
//    	return "home/signin";
//    }
//    
//	@PostMapping("/sigin/save")
//	public String sigin(Model model, @ModelAttribute("accounts") Account account) {
//		Account currentUser = accountsRepository.getByEmail(account.getEmail());
//		if (currentUser != null) {
//			session.setAttribute("currentUsername", currentUser.getEmail());
//		}
//
//		session.setAttribute("user", currentUser);
//		System.out.println("user");
//		return "redirect:/index";
//	}
//	
//    @GetMapping("/change")
//    public String Change() {
//        return "home/change_password";
//    }
//
//    @GetMapping("/forgot")
//    public String Forgot() {
//        return "home/forgot_password";
//    }
//
//    @GetMapping("/information")
//    public String Information() {
//        return "home/information";
//    }
//    
//    @GetMapping("/logout")
//    public String logout() {
//    	return "forward:/signin";
//    }
//}

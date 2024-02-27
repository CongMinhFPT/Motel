package com.motel.controller;

import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.swing.text.Style;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import com.motel.service.AuthorityService;
import com.motel.service.MailerService;


@Controller
public class RegisterController {
	
	@Autowired 
	AccountsRepository accountsRepository;
    
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	MailerService mailerService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	BCryptPasswordEncoder pe;
	
   
    
    @GetMapping("/signup")
    public String Signup(@ModelAttribute("accounts") Account account) {
        return "home/signup";
    }
    
    @PostMapping("/register/save")
    public String save(@Validated @ModelAttribute("accounts") Account account, BindingResult bindingResult, @RequestParam("password1") String pass, Model model) {
    	if(bindingResult.hasErrors()) {
    		if(account.getPhone().isBlank()) {
    			model.addAttribute("phone","Vui lòng nhập số điện thoại!");
    		}
    		if(pass.isEmpty()) {
    			model.addAttribute("pass","Vui lòng nhập xác nhận mật khẩu!");
    		}
    		return "home/signup";
    	}
    	
    	if(!account.getPhone().matches("^(0[2|3|5|7|8|9])+([0-9]{8})")) {
    		model.addAttribute("phone", "Sai định dạng số điện thoại!");
    		return "home/signup";
    	}
    	if(!account.getPassword().equals(pass)) {
    		model.addAttribute("pass","Xác nhận mật khẩu không đúng!");
    		return "home/signup";
    	}
    	if(accountsRepository.getByEmail(account.getEmail()) != null) {
    		model.addAttribute("email", "Email này đã tồn tại!");
    		return "home/signup";
    	}
    	
    	String passw = account.getPassword();
    	System.out.println("passw>> " + passw);
    	String passpe = pe.encode(passw);
    	System.out.println("PassPE>> " + passpe);
    	account.setPassword(passpe);
    	accountsRepository.save(account);
    	
    	Role staff = roleRepository.findById("STAFF").orElseGet(() -> {
    		Role newRole = new Role();
    		newRole.setId("STAFF");
    		return roleRepository.save(newRole);
    	});
    	
    	Authority au = new Authority();
    	au.setAccount(account);
    	au.setRole(staff);
    	authorityRepository.save(au);
    	
    	mailerService.add(memeMessage -> {
    		MimeMessageHelper helper = new MimeMessageHelper(memeMessage);
    		try {
				helper.setTo(account.getEmail());
				helper.setSubject("Nhà Trọ F.E Xin Chào!");
				helper.setText("Nhà trọ F.E luôn là lựa chọn tốt nhất. Hân hạnh được phục vụ quí khách!");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
    	});
    	
    	model.addAttribute("create", "Thêm mới thành công!");
    
    	return "redirect:/signin";
    }
    
    @RequestMapping("/signin")
    public String error(Model model, @RequestParam(required = false) String error) {
    	if (error != null) {
			model.addAttribute("error", "Đăng nhập thất bại!");
		}
        return "home/signin";

    }
    
    @RequestMapping("/auth/access/denied")
    public String denied(Model model) {
    	model.addAttribute("error", "Bạn không có quyền truy cập!");
    	return "home/signin";
    }
    
	@PostMapping("/sigin/save")
	public String sigin(Model model, @ModelAttribute("accounts") Account account) {
		Account currentUser = accountsRepository.getByEmail(account.getEmail());
		if (currentUser != null) {
			session.setAttribute("currentUsername", currentUser.getEmail());
		}

		session.setAttribute("user", currentUser);
		System.out.println("user");
		return "redirect:/index";
	}
	
	@RequestMapping("/oauth2/login/success")
	public String success(OAuth2AuthenticationToken oauth2, Model model) {
		authorityService.loginFromOAuth2(oauth2);
		OAuth2User oauth2User = oauth2.getPrincipal();
//		String username = oauth2User.getAttribute("email");
		String email = oauth2User.getAttribute("email");
		System.out.println("Email>> " + email);
		int leftLimit = 97; // 'a'
		int righLimit = 122; // 'z'
		int len = 8;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (righLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		System.out.println("pass>> " + generatedString);
		Account acc = new Account();
		acc.setEmail(email);
		acc.setFullname(email);
		acc.setPassword(pe.encode(generatedString));
		acc.setActive(true);
		acc.setPhone("null");
		if(accountsRepository.getByEmail(email) != null) {
			model.addAttribute("error", "Email này đã tồn tại vui lòng trọn email khác!");
			return "redirect:/index";
		}else {
			accountsRepository.save(acc);
			Role staff = roleRepository.findById("STAFF").orElseGet(() -> {
	    		Role newRole = new Role();
	    		newRole.setId("STAFF");
	    		return roleRepository.save(newRole);
	    	});
	    	
	    	Authority au = new Authority();
	    	au.setAccount(acc);
	    	au.setRole(staff);
	    	authorityRepository.save(au);
		}

		return "forward:/index";
	}
	
	//
    @GetMapping("/change")
    public String Change() {
        return "home/change_password";
    }

    @GetMapping("/forgot")
    public String Forgot() {
        return "home/forgot_password";
    }

   
    
    @PostMapping("/forgot/save")
    public String ForgotSubmit(Model model, @RequestParam("email") String email) {
    	Account currentAcc = accountsRepository.getByEmail(email);
    	Integer id = (currentAcc != null) ? currentAcc.getAccountId() : null;
    	if(email.isBlank()) {
    		model.addAttribute("email","Vui lòng nhập email!");
    		return "home/forgot_password";
    	}
    	if(id != null) {
    		int leftLimit = 97; 
    		int rightLimit = 122;
    		int len = 8;
    		Random random = new Random();
    		StringBuilder buffer = new StringBuilder(len);
    		for(int i = 0; i < len; i++) {
    			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
    			buffer.append((char) randomLimitedInt);
    		}
    		String generatedString = buffer.toString();
    		System.out.println("Random forgot:>> " + generatedString);
    		
    		Account acc = accountsRepository.getByEmail(email);
    		acc.setPassword(pe.encode(generatedString));
    		accountsRepository.save(acc);
    		try {
				sendEmail(email, generatedString);
				model.addAttribute("message", "Vui lòng xem gmail để lấy lại mật khẩu!");
				return "home/forgot_password";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
    	}else {
    		model.addAttribute("message","Email không tồn tại!");
    		return "home/forgot_password";
    	}
    	return "home/forgot_password";
    }
    
    @GetMapping("/information")
    public String Information() {
        return "home/information";
    }
    
    @GetMapping("/logout")
    public String logout() {
    	return "forward:/signin";
    }
    
    private void sendEmail(String email, String password) {
		// gửi email
		mailerService.add(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			try {
				helper.setTo(email);
				helper.setSubject("Nhà Trọ F.E Xin Chào!");
				helper.setText("Bạn đã quên mật khẩu cũ và đã yêu cầu một mật khẩu mới: <br/>Đây là mật khẩu mới của bạn: <span style='font-size: 18px; color: red'>" + password + "</span>.", true);

			} catch (MessagingException e) {
				// TODO: handle exception

			}

		});
	}
}

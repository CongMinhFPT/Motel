package com.motel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.motel.entity.Account;
import com.motel.repository.AccountsRepository;

@Controller
public class MessageController {
	@Autowired
	AccountsRepository accountsRepository;

	@GetMapping("/texting/{emailPoster}")
	public String chat(@PathVariable("emailPoster") String emailPoster,
			Model model, Authentication authentication) {
		String acc = authentication.getName();
		model.addAttribute("acc", acc);
		Account accc = accountsRepository.getByEmail(acc);
		model.addAttribute("account", accc);
		model.addAttribute("emailPoster", emailPoster);
	
		return "chat/chat";
	}
}

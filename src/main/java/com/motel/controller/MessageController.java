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

	@GetMapping("/chat/{email}/{emailPoster}")
	public String chat(@PathVariable("email") String email, @PathVariable("emailPoster") String emailPoster,
			Model model, Authentication authentication) {
		Account accc = accountsRepository.getByEmail(email);
		model.addAttribute("account", accc);
		model.addAttribute("emailPoster", emailPoster);
		String acc = authentication.getName();
		model.addAttribute("acc", acc);
		return "chat/chat";
	}
}

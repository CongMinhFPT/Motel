package com.motel.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.repository.AccountsRepository;
import com.motel.repository.RenterRepository;
import com.motel.repository.RequestAuthorityRepository;
import com.motel.entity.Account;
import com.motel.entity.Renter;
import com.motel.service.RenterService;

@Controller
public class AdminController {

	@Autowired
	RenterService renterService;
	@Autowired
	RequestAuthorityRepository requestAuthorityRepository;

	@Autowired
    RenterRepository renterRepository;

    @Autowired
    AccountsRepository accountsRepository;

	@GetMapping("/admin")
	public String index(Model model, Authentication authentication) {
		String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        model.addAttribute("accountIdStatistic", account.getAccountId());
		int count = requestAuthorityRepository.findRequestCount();
		model.addAttribute("requestCount", count);
		return "admin/home/index";
	}

	@GetMapping("/authority")
	public String authority(Model model) {
		int count = requestAuthorityRepository.findRequestCount();
		model.addAttribute("requestCount", count);
		return "admin/authority/auth";
	}
}

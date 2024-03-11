package com.motel.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.motel.entity.Account;
import com.motel.entity.Authority;
import com.motel.repository.AccountsRepository;
import com.motel.repository.AuthorityRepository;
import com.motel.service.AccountService;

@Controller
public class AdminCustomer {

	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AuthorityRepository authorityRepository;
	@GetMapping("/customerList")
	public String showList(Model model) {
		List<Account> acc = accountsRepository.findAll();
		model.addAttribute("customer",acc);
		return "admin/customers/customerList";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id,Model model) {
		Account acc = accountService.findById(id).orElseThrow(() -> new IllegalArgumentException("Id không tồn tại: " + id));
		acc.setActive(false);
		accountsRepository.save(acc);
		return "redirect:/customerList";
	}
	@GetMapping("/customerList2")
	public String showList2(Model model) {
		List<Authority> acc = authorityRepository.findAll();
		model.addAttribute("customer",acc);
		return "admin/customers/customerList2";
	}
}

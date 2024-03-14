package com.motel.restcontroller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motel.entity.Account;
import com.motel.entity.Authority;
import com.motel.repository.AccountsRepository;
import com.motel.repository.AuthorityRepository;
import com.motel.repository.RoleRepository;

@CrossOrigin("*")
@RestController
public class AuthorityRestController {
	
	@Autowired
	AccountsRepository accountsRepository;
	@Autowired
	AuthorityRepository authorityRepository;
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/rest/authorities")
	public Map<String, Object> getAuthority(){
		Map<String, Object> data = new HashMap<>();
		data.put("authorities", authorityRepository.findAll());
		data.put("accounts", accountsRepository.findAll());
		data.put("roles", roleRepository.findAllRole());
		return data;
	}
	@GetMapping("/api/account")
	public Principal getAccount(Authentication authentication) {
		return authentication;
	}
	
	
	@PostMapping("/rest/authorities")
	public Authority create(@RequestBody Authority accRole) {
		return authorityRepository.save(accRole);
	}
	
	@DeleteMapping("/rest/authorities/{id}")
	public void delete(@PathVariable("id") Integer id) {
		authorityRepository.deleteById(id);
	}
	
}

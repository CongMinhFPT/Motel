package com.motel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.repository.AccountsRepository;

@Service
public class AuthorityService implements UserDetailsService{

	@Autowired
	AccountsRepository accountsRepository;
	
//	@Autowired
//	BCryptPasswordEncoder pe;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account acc = accountsRepository.getByEmail(username);
			System.out.println("Email>> " + acc);
			String password = acc.getPassword();
			int accountid = acc.getAccountId();
			System.out.println("Pass>> " + password);
			String[] roles = acc.getAuthorities().stream()
							.map(au -> au.getRole().getId())
							.collect(Collectors.toList()).toArray(new String[0]);
			System.out.println("role>> "+ roles);
			List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
			for (String role : roles) {
				Assert.isTrue(!role.startsWith("ROLE_"),
						() -> role + " cannot start with ROLE_ (it is automatically added)");
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			CustomUserDetails userDetails = new CustomUserDetails(username, password,authorities,accountid, 0);
			return userDetails;
		} catch (Exception e) {
			System.out.println("Không tồn tại tài khoản này " + username);
			System.out.println("Đã xảy ra lỗi khi tìm kiếm tài khoản: " + e.getMessage());
			e.printStackTrace();
			throw new UsernameNotFoundException(username + "not found!");
		}
	}

}

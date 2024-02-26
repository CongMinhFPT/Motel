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
			List<GrantedAuthority> grandList = new ArrayList<GrantedAuthority>();
			if(roles!=null) {
			for(String role: roles) {
				System.out.println(role);
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grandList.add(authority);
			}
		}
			CustomUserDetails userDetails = new CustomUserDetails(username, password,grandList,accountid, 0);
			return userDetails;
		} catch (Exception e) {
			System.out.println("Không tồn tại tài khoản này " + username);
			System.out.println("Đã xảy ra lỗi khi tìm kiếm tài khoản: " + e.getMessage());
			e.printStackTrace();
			throw new UsernameNotFoundException(username + "not found!");
		}
	}

}

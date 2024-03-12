package com.motel.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.repository.AccountsRepository;

@Service
public class AuthorityService implements UserDetailsService{

	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	AccountService accountService;
//	@Autowired
//	BCryptPasswordEncoder pe;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account acc = accountsRepository.getByEmail(username);
			System.out.println("Email>> " + acc);
			String password = acc.getPassword();
			System.out.println("Pass>> " + password);
			String[] roles = acc.getAuthorities().stream()
							.map(au -> au.getRole().getId())
							.collect(Collectors.toList()).toArray(new String[0]);
			System.out.println("role>> "+ roles);
			return User.withUsername(username).password(password).roles(roles).build();
		} catch (Exception e) {
			System.out.println("Không tồn tại tài khoản này " + username);
			System.out.println("Đã xảy ra lỗi khi tìm kiếm tài khoản: " + e.getMessage());
			e.printStackTrace();
			throw new UsernameNotFoundException(username + "not found!");
		}
	}

	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
		//đọc thông tin từ mạng xã hội 
		String email = oauth2.getPrincipal().getAttribute("email");
		String password = Long.toHexString(System.currentTimeMillis());
		//Tạo đối tượng UserDetails
		UserDetails user = User.withUsername(email)
							.password(password)
							.roles("STAFF").build();
		//tạo đối tượng authentication từ userDetail
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		//Thay đổi thông tin đăng nhập từ hệ thống
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}

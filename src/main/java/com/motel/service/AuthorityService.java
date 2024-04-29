package com.motel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import org.springframework.util.Assert;

import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.repository.AccountsRepository;

@Service
public class AuthorityService implements UserDetailsService{

	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	AccountService accountService;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 Account acc = accountsRepository.getByEmail(username);
		    if (acc != null && acc.isActive()==true) {
		        String password = acc.getPassword();
				int accountid = acc.getAccountId();
		        String[] roles1 = acc.getAuthorities().stream()
		                        .map(au -> au.getRole().getId())
		                        .collect(Collectors.toList())
		                        .toArray(new String[0]);
			List<GrantedAuthority> roles = new ArrayList<>(roles1.length);
			for (String role : roles1) {
				Assert.isTrue(!role.startsWith("ROLE_"),
						() -> role + " cannot start with ROLE_ (it is automatically added)");
						roles.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			CustomUserDetails userDetails = new CustomUserDetails(username, password,roles,accountid, 0);
			return userDetails;
		    } else {
		        return null; // Trả về null nếu không tìm thấy tài khoản
		    }

	}

	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2) {
		//đọc thông tin từ mạng xã hội 
		String email = oauth2.getPrincipal().getAttribute("email");
		
		//Tạo đối tượng UserDetails
		 UserDetails userDetails = loadUserByUsername(email);
		    if (userDetails == null) {
		        // Nếu người dùng chưa tồn tại, tạo một người dùng mới với quyền "CUSTOMER"
		        userDetails = User.withUsername(email)
		                          .password(Long.toHexString(System.currentTimeMillis()))
		                          .roles("CUSTOMER")
		                          .build();
		    }
            
		    // Tạo đối tượng authentication từ UserDetails
		    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		    // Thay đổi thông tin đăng nhập từ hệ thống
		    SecurityContextHolder.getContext().setAuthentication(auth);
	}
}

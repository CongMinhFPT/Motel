package com.motel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.motel.service.AuthorityService;

@Configuration
@EnableWebSecurity
public class AuthorityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	AuthorityService authorityService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authorityService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable();

		http.authorizeRequests()
				.antMatchers("/authority", "/requestauth", "/editrequest/**", "/requestauth/**", "/customerList",
						"/delete/**", "/edit/**", "/add/**")
				.hasRole("SUPPER")
				.antMatchers("/admin/blogs","/admin/add-blog","/admin/edit-blog/**","/admin/remove-blog/**","/admin/save-blog/**","/admin/tags","/admin/add-tag","/admin/edit-tag/**",
						"/admin/remove-tag/**","/admin/save-tag","/admin/check-posts","/admin/check-update-post/**",
						"/admin/check-update-post/**").hasAnyRole("MANAGER")
				.antMatchers("/admin/posts","/admin/add-post","/admin/update-post/**","/admin/save-post","/admin/update-post/**","/admin/deletePost/**",
						"/admin/show-motel","/admin/add-motel","/admin/manage-motel","/admin/manage-motel/**","/admin/update-motel","/admin/condition-motel","/admin/add-motelroom",
						"/admin/manage-motelroom","/admin/update-motelroom/**","/admin/invoice/**","/admin/renter/**","/admin/deleteRenter/**","/admin/indexs/**",
						"/admin/category","/admin/categoryform/**","/admin/addcategory","/admin/upcategory","/changeStatus/**").hasAnyRole("OWNER")
				.antMatchers("/admin/**").hasAnyRole("MANAGER","SUPPER","OWNER")
//				.antMatchers("/room","/change/**","/information/**","/payment/**","/payment_infor").hasAnyRole("MANAGER", "SUPPER", "OWNER", "CUSTOMER")
				.antMatchers("/change/**","/information/**","/payment/**","/payment_infor","/request","/deleteRe/**","/requestList","/change/**").authenticated()
				.anyRequest().permitAll();

		http.exceptionHandling()
				.accessDeniedPage("/auth/access/denied");

		http.formLogin()
				.loginPage("/signin")
				.loginProcessingUrl("/signin/save")
				.defaultSuccessUrl("/index", false)
				.failureUrl("/signin?error=true")
				.usernameParameter("email")
				.passwordParameter("password");

		http.logout()
				.logoutUrl("/auth/logoff")
				.logoutSuccessUrl("/logout");

		http.oauth2Login()
				.loginPage("/signin")
				.defaultSuccessUrl("/oauth2/login/success", true)
				.failureUrl("/signin?error=true")
				.authorizationEndpoint()
				.baseUri("/oauth2/authorization");

	}

}

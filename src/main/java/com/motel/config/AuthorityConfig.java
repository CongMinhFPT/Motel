package com.motel.config;
import com.motel.service.AuthorityService;

@Configuration
@EnableWebSecurity
public class AuthorityConfig extends WebSecurityConfigurerAdapter{

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
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().cors().disable();
		
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("MANAGER")
			.antMatchers("/admin").hasAnyRole("MANAGER","STAFF")
			.antMatchers("/information/**","/news/**","/news_details/**","/room-detail/**").authenticated()
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

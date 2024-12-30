package com.shopapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopapp.security.oauth.CustomerOAuth2UserService;
import com.shopapp.security.oauth.LoginOAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private CustomerOAuth2UserService auth2UserService;
	
	@Autowired
	private LoginOAuth2SuccessHandler oauth2Handler;
	
	@Autowired
	private DatabaseLoginSuccessHandler dbHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CustomerUserDetailsService customerUserDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customerUserDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/account_details", "/update_account_details", "/cart",
						"/address_book/**")
				.authenticated()
				.anyRequest()
				.permitAll())
			.formLogin(log -> log
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(dbHandler)
				.permitAll())
			.oauth2Login(oal -> oal
				.loginPage("/login")
				.userInfoEndpoint(uiep -> uiep.userService(auth2UserService))
				.successHandler(oauth2Handler))	
			.logout(log -> log.permitAll())
			.rememberMe(rem -> rem
				.key("abcdeflfdasfkjkafs_0123456789")
				.tokenValiditySeconds(14 * 24 * 60 * 60))
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**", "/webjars/**");
	}
}

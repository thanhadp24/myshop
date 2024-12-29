package com.shopapp.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopappUserDetailService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/users/**", "/setting/**", "/countries/**", "/states/**").hasAuthority("Admin")
				.requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin","Editor")
					
				.requestMatchers("/products/delete/**", "/products/new/**")
					.hasAnyAuthority("Admin", "Editor")
				.requestMatchers("/products/edit/**", "/products/save/**", "/products/check_unique")
					.hasAnyAuthority("Admin", "Editor", "Salesperson")
				.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
					.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
				.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
				
				.requestMatchers("/questions/**", "/reviews/**").hasAnyAuthority("Admin", "Assistant")
				.requestMatchers("/customers/**", "/shipping/**", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
				.requestMatchers("/orders/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
				.requestMatchers("/articles/**", "/menu/**").hasAnyAuthority("Admin", "Editor")
				.anyRequest()
				.authenticated())
		.formLogin(login -> login
					.loginPage("/login")
					.usernameParameter("email")
					.permitAll())
		.logout(logout -> logout.permitAll())
		.rememberMe(rem -> rem	   		// default remember-me: only login automatically when reopen brower
				.key("AbcDefgHijKlmnOpqrs_1234567890")  // hash-based token implement: allow restart app or
				.tokenValiditySeconds(7 * 24 * 60 * 60) // reopen web brower it's always login automatically
				);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/css/**", "/webjars/**", "/richtext/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

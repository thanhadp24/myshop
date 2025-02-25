package com.shopapp.admin.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopapp.admin.repository.UserRepository;
import com.shopapp.common.entity.User;

public class ShopappUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.getUserByEmail(email);
		if(user != null) {
			return new ShopappUserDetails(user);
		}
		
		throw new UsernameNotFoundException("Could not find user with email " + email);
	}
}

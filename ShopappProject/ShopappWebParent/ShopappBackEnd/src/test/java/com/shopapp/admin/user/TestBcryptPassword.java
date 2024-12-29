package com.shopapp.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBcryptPassword {
	
	@Test
	public void testBcrypt() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String pass = encoder.encode("thanh11");
		
		System.out.println(pass);
	}
}

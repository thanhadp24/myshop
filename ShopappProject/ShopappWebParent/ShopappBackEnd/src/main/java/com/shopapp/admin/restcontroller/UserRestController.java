package com.shopapp.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.service.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkUniqueEmail(Integer id, String email) {
		return userService.isEmailUnique(id, email) ? "OK" : "Email is existed!!!";
	}
}

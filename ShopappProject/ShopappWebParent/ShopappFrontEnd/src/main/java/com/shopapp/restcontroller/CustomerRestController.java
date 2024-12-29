package com.shopapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.service.CustomerService;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customers/check_unique_email")
	public String checkUnique(String email) {
		return customerService.isUniqueEmail(email) ? "OK" : "Duplicated";
	}
}

package com.shopapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopapp.common.entity.Customer;
import com.shopapp.service.CustomerService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ControllerHelper {
	
	@Autowired
	private CustomerService customerService;
	
	public Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);
		return customerService.getByEmail(customerEmail);
	}
}

package com.shopapp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopapp.common.entity.Customer;
import com.shopapp.common.enumm.AuthenticationType;
import com.shopapp.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	@Lazy
	private CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerUserDetails cud = (CustomerUserDetails) authentication.getPrincipal();
		
		Customer customer = cud.getCustomer();
		
		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}

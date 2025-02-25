package com.shopapp.security.oauth;

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
public class LoginOAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	@Autowired
	@Lazy
	private CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();
		String email = oauth2User.getEmail();
		String name = oauth2User.getName();
		String countryCode = request.getLocale().getCountry();
		
		String clientName = oauth2User.getClientName();
		AuthenticationType authenticationType = getAuthenticationType(clientName);
		
		Customer customer = customerService.getByEmail(email);
		if(customer == null) {
			customerService.addCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
		}else {
			oauth2User.setFullName(customer.getFullName());
			customerService.updateAuthenticationType(customer, authenticationType);
		}
	
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private AuthenticationType getAuthenticationType(String clientName) {
		if(clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		}else if(clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		}else {
			return AuthenticationType.DATABASE;
		}
	}
}

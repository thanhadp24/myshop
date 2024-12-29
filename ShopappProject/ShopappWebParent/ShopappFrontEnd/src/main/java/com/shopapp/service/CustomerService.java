package com.shopapp.service;

import java.util.List;

import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.enumm.AuthenticationType;
import com.shopapp.common.exception.CustomerNotFoundException;

public interface CustomerService {

	List<Country> getAllCountries();

	boolean isUniqueEmail(String email);

	void registerCustomer(Customer customer);

	boolean verify(String verificationCode);

	Customer getByEmail(String email);

	void updateAuthenticationType(Customer customer, AuthenticationType type);

	void addCustomerUponOAuthLogin(String name, String email, String countryCode,
			AuthenticationType authenticationType);
	
	void update(Customer customerInForm);

	String updateResetPasswordToken(String email) throws CustomerNotFoundException;

	Customer getByResetToken(String token);
	
	void updatePassword(String token, String newPassword) throws CustomerNotFoundException;
}

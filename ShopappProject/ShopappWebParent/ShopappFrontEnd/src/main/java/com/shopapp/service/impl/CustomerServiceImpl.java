package com.shopapp.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.enumm.AuthenticationType;
import com.shopapp.common.exception.CustomerNotFoundException;
import com.shopapp.repository.CountryRepository;
import com.shopapp.repository.CustomerRepository;
import com.shopapp.service.CustomerService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired PasswordEncoder encoder;
	
	@Override
	public List<Country> getAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	@Override
	public boolean isUniqueEmail(String email) {
		return customerRepository.findByEmail(email) == null;
	}

	@Override
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setCreatedAt(new Date());
		customer.setEnabled(false);
		customer.setType(AuthenticationType.DATABASE);
		
		String random = RandomStringUtils.randomAlphabetic(64);
		customer.setVerificationCode(random);
		
		customerRepository.save(customer);
	}
	
	@Override
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);
		
		if(customer == null || customer.isEnabled()) {
			return false;
		}else {
			customerRepository.enable(customer.getId());
			return true;
		}
		
	}
	
	@Override
	public Customer getByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	
	@Override
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if(!customer.getType().equals(type)) {
			customerRepository.updateAuthenticationType(customer.getId(), type);
		}
	}

	@Override
	public void addCustomerUponOAuthLogin(String name, String email, String countryCode,
			AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		
		setName(name, customer);
		
		customer.setEnabled(true);
		customer.setCreatedAt(new Date());
		customer.setPassword("");
		customer.setType(authenticationType);
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPostalCode("");
		customer.setPhoneNumber("");
		customer.setCountry(countryRepository.findByCode(countryCode));
		
		customerRepository.save(customer);
	}
	
	@Override
	public void update(Customer customerInForm) {
		Customer customerInDb = customerRepository.findById(customerInForm.getId()).get();
		
		if(customerInDb.getType().equals(AuthenticationType.DATABASE)) {
			if(customerInForm.getPassword().isEmpty()) {
				customerInForm.setPassword(customerInDb.getPassword());
			}else {
				customerInForm.setPassword(encoder.encode(customerInForm.getPassword()));
			}
		}else {
			customerInForm.setPassword(customerInDb.getPassword());
		}
		
		customerInForm.setEnabled(customerInDb.isEnabled());
		customerInForm.setCreatedAt(customerInDb.getCreatedAt());
		customerInForm.setVerificationCode(customerInDb.getVerificationCode());
		customerInForm.setType(customerInDb.getType());
		customerInDb.setResetPasswordToken(customerInDb.getResetPasswordToken());
		
		customerRepository.save(customerInForm);
	}
	
	private void setName(String name, Customer customer) {
		String[] nameAsArray = name.split(" ");
		if(nameAsArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName = nameAsArray[0];
			customer.setFirstName(firstName);
			customer.setLastName(name.replaceFirst(firstName + " ", ""));
		}
	}

	private void encodePassword(Customer customer) {
		String encodedPass = encoder.encode(customer.getPassword());
		customer.setPassword(encodedPass);
	}

	@Override
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		
		if(customer != null) {
			String resetToken = RandomStringUtils.randomAlphabetic(30);
			customer.setResetPasswordToken(resetToken);
			customerRepository.save(customer);
			
			return resetToken;
		}else {
			throw new CustomerNotFoundException("could not find any customer with email: " + email);
		}
	}

	@Override
	public Customer getByResetToken(String token){
		return customerRepository.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByResetPasswordToken(token);
		if(customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}
		
		customer.setPassword(encoder.encode(newPassword));
		customer.setResetPasswordToken(null);
		customerRepository.save(customer);
	}
}

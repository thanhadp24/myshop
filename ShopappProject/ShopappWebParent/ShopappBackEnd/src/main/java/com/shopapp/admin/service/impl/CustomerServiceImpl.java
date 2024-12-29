package com.shopapp.admin.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.exception.CustomerNotFoundException;
import com.shopapp.admin.repository.CountryRepository;
import com.shopapp.admin.repository.CustomerRepository;
import com.shopapp.admin.service.CustomerService;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerServiceImpl  implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public Page<Customer> getByPage(int pageNum, String keyword, String sortDir, String sortField) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, Common.CUSTOMERS_PER_PAGE, sort);
		
		if(keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}
		
		return customerRepository.findAll(pageable);
	}

	@Override
	public void updateEnabledStatus(Integer id, boolean status) {
		customerRepository.updateEnableStatus(id, status);
	}

	@Override
	public Customer get(int id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customer with id: " + id);
		}
	}

	@Override
	public void save(Customer customerInForm) {
		Customer customerInDb = customerRepository.findById(customerInForm.getId()).get();
		
		if(customerInForm.getPassword().isEmpty()) {
			customerInForm.setPassword(customerInDb.getPassword());
		}else {
			customerInForm.setPassword(encoder.encode(customerInForm.getPassword()));
		}
		
		customerInForm.setEnabled(customerInDb.isEnabled());
		customerInForm.setCreatedAt(customerInDb.getCreatedAt());
		customerInForm.setVerificationCode(customerInDb.getVerificationCode());
		customerInForm.setResetPasswordToken(customerInDb.getResetPasswordToken());
		
		customerRepository.save(customerInForm);
	}

	@Override
	public void delete(Integer id) throws CustomerNotFoundException {
		Long countById = customerRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new CustomerNotFoundException("Could not find any customer with id: " + id);
		}
		
		customerRepository.deleteById(id);
	}

	@Override
	public boolean isEmailUnique(Integer id, String email) {
		Customer customer = customerRepository.findByEmail(email);
		
		if(customer != null && customer.getId() != id) {
			return false;
		}
		
		return true;
	}
		
	@Override
	public List<Country> getAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	
}

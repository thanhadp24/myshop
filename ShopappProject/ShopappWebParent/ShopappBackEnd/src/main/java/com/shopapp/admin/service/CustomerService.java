package com.shopapp.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopapp.admin.exception.CustomerNotFoundException;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.Customer;

public interface CustomerService {
	
	Page<Customer> getByPage(int pageNum, String keyword, String sortDir, String sortField);
	
	void updateEnabledStatus(Integer id, boolean status);
	
	Customer get(int id) throws CustomerNotFoundException;
	
	void save(Customer customerInForm);
	
	void delete(Integer id) throws CustomerNotFoundException;
	
	boolean isEmailUnique(Integer id, String email);
	
	List<Country> getAllCountries();
}

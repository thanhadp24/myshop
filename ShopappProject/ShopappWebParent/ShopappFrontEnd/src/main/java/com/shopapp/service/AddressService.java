package com.shopapp.service;

import java.util.List;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;

public interface AddressService {

	List<Address> getAddressesBook(Customer customer);

	Address get(Integer customerId, Integer addressId);
	
	void save(Address address);
	
	void delete(Integer customerId, Integer addressId);

	void setDefaultAddress(Integer defaultAddressId, Integer customerId);
	
	Address getDefaultByCustomer(Customer customer);
}

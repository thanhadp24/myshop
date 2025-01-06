package com.shopapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;
import com.shopapp.repository.AddressRepository;
import com.shopapp.service.AddressService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService{

	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public List<Address> getAddressesBook(Customer customer) {
		return addressRepository.findByCustomer(customer);
	}
	
	@Override
	public Address get(Integer customerId, Integer addressId) {
		return addressRepository.findByIdAndCustomer(customerId, addressId);
	}

	@Override
	public void save(Address address) {
		addressRepository.save(address);
	}

	@Override
	public void delete(Integer customerId, Integer addressId) {
		addressRepository.deleteByIdAndCustomer(customerId, addressId);
	}
	
	@Override
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if(defaultAddressId > 0) {
			addressRepository.setDefaultAddress(defaultAddressId);
		}
		addressRepository.setNoneDefault4Others(defaultAddressId, customerId);
	}
	
	@Override
	public Address getDefaultByCustomer(Customer customer) {
		return addressRepository.findDefaultByCustomer(customer.getId());
	}
}

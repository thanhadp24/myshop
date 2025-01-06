package com.shopapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.ShippingRate;
import com.shopapp.repository.ShippingRateRepository;
import com.shopapp.service.ShippingRateService;

@Service
public class ShippingRateServiceImpl implements ShippingRateService{

	@Autowired
	private ShippingRateRepository rateRepository;
	
	@Override
	public ShippingRate getForCustomer(Customer customer) {
		String state = customer.getState();
		if(state == null || state.isEmpty()) {
			state = customer.getCity();
		}
		
		return rateRepository.findByCountryAndState(customer.getCountry(), state);
	}

	@Override
	public ShippingRate getForAddress(Address address) {
		String state = address.getState();
		if(state == null || state.isEmpty()) {
			state = address.getCity();
		}
		
		return rateRepository.findByCountryAndState(address.getCountry(), state);
	}
}

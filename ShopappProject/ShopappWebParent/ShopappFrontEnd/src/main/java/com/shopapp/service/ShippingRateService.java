package com.shopapp.service;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.ShippingRate;

public interface ShippingRateService {
	
	ShippingRate getForCustomer(Customer customer);

	ShippingRate getForAddress(Address address);
}

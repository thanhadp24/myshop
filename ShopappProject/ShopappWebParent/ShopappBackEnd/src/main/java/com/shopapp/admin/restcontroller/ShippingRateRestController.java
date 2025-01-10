package com.shopapp.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.exception.ShippingRateNotFoundException;
import com.shopapp.admin.service.ShippingRateService;

@RestController
public class ShippingRateRestController {
	
	@Autowired
	private ShippingRateService rateService;
	
	@PostMapping("/get_shipping_cost")
	public String getShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
		float shippingCost = rateService.calculateShippingCost(productId, countryId, state);
		return String.valueOf(shippingCost);
	}
	
}

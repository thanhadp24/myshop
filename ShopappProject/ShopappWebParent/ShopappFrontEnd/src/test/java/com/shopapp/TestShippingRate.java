package com.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.ShippingRate;
import com.shopapp.repository.ShippingRateRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TestShippingRate {

	@Autowired 
	private ShippingRateRepository rateRepository;
	
	@Test 
	public void testGet() {
		ShippingRate shippingRate = rateRepository.findByCountryAndState(new Country(240), "Đà Nẵng");
		System.out.println(shippingRate);
	}
}

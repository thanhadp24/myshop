package com.shopapp.admin.shippingrate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopapp.admin.exception.ShippingRateNotFoundException;
import com.shopapp.admin.repository.ProductRepository;
import com.shopapp.admin.repository.ShippingRateRepository;
import com.shopapp.admin.service.ShippingRateService;
import com.shopapp.admin.service.impl.ShippingRateServiceImpl;
import com.shopapp.common.entity.ShippingRate;
import com.shopapp.common.entity.product.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class TestShippingRate {
	
	@MockBean private ShippingRateRepository rateRepository;
	@MockBean private ProductRepository productRepository;
	
	@InjectMocks
	private ShippingRateServiceImpl rateService;
	
	@Test
	public void testCalculateShippingCost_NoRateFound() {
		
		Mockito.when(rateRepository.findByCountryAndState(234, "ABC")).thenReturn(null);
		
		assertThrows(ShippingRateNotFoundException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				rateService.calculateShippingCost(10, 234, "ABC");
			}
		});
	}
	
	@Test
	public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
		Integer productId = 10;
		Integer countryId = 233;
		String state = "New York";
		
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(5);
		
		Mockito.when(rateRepository.findByCountryAndState(countryId, state))
		.thenReturn(shippingRate);
		
		Product product = new Product();
		product.setWidth(5);
		product.setHeight(3);
		product.setLength(4);
		product.setWeight(3);
		
		Mockito.when(productRepository.findById(productId))
		.thenReturn(Optional.of(product));
		
		float shippingCost = rateService.calculateShippingCost(productId, countryId, state);
		assertEquals(15, shippingCost);
	}
}

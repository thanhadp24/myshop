package com.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.enumm.OrderStatus;
import com.shopapp.repository.OrderDetailRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestOrderDetail {

	@Autowired
	private OrderDetailRepository detailRepository;
	
	@Test
	public void testCountByProductAndCustomerAndOrderstatus() {
		Integer productId = 2;
		Integer customerId = 3;
		
		Long count = detailRepository.countByProductAndCustomerAndOrderStatus(productId, customerId, OrderStatus.DELIVERED);
		System.out.println(count);
	}
}

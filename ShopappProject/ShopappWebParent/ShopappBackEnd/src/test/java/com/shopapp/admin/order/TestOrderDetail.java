package com.shopapp.admin.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.OrderDetailRepository;
import com.shopapp.common.entity.order.OrderDetail;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestOrderDetail {

	@Autowired
	private OrderDetailRepository detailRepository;
	
	@Test
	public void testFindCategoryWithTimeBetween() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse("2025-01-10");
		Date end = df.parse("2025-01-19");
		
		var orderDetails = detailRepository.findWithCategoryAndTimeBetween(start, end);
		
		for(OrderDetail od: orderDetails) {
			System.out.printf("%s | %s | %.2f | %.2f | %.2f", od.getProduct().getCategory().getName(),
					od.getQuantity(), od.getShippingCost(), od.getProductCost(), od.getSubtotal());
			System.out.println();
		}
	}
	
	@Test
	public void testFindProductWithTimeBetween() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse("2025-01-10");
		Date end = df.parse("2025-01-19");
		
		var orderDetails = detailRepository.findWithProductAndTimeBetween(start, end);
		
		for(OrderDetail od: orderDetails) {
			System.out.printf("%d \t | %30s | %10.2f \t | %10.2f \t | %10.2f", od.getQuantity(),
					od.getProduct().getName(), od.getShippingCost(), 
					od.getProductCost(), od.getSubtotal());
			System.out.println();
		}
	}
}

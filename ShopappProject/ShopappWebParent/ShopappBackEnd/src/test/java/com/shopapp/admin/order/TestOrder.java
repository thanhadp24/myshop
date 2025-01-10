package com.shopapp.admin.order;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.OrderRepository;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.entity.order.OrderDetail;
import com.shopapp.common.entity.order.OrderTrack;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.enumm.OrderStatus;
import com.shopapp.common.enumm.PaymentMethod;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestOrder {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreate() {
		Customer customer = entityManager.find(Customer.class, 3);
		Product product = entityManager.find(Product.class, 4);
		Order order = new Order();
		
		order.setOrderTime(new Date());
		order.setCustomer(customer);
		
		order.copyAddressFromCustomer();
		
		order.setDeliverDate(new Date());
		order.setDeliverDays(3);
		order.setState(customer.getState());
		
		order.setProductCost(product.getCost());
		order.setTax(0);
		order.setSubtotal(product.getPrice());
		order.setShippingCost(10);
		order.setTotal(product.getPrice() + 10);
		
		order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		order.setOrderStatus(OrderStatus.PACKAGED);
		
		OrderDetail detail = new OrderDetail();
		detail.setOrder(order);
		detail.setProduct(product);
		detail.setQuantity(3);
		detail.setShippingCost(10);
		detail.setSubtotal(product.getPrice() * 3);
		detail.setUnitPrice(product.getPrice());
		detail.setProductCost(product.getCost());
		
		order.getOrderDetails().add(detail);
		
		orderRepository.save(order);
	}
	
	@Test
	public void testUpdateOrderTracks() {
		Order order = orderRepository.findById(10).get();
		
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setOrder(order);
		orderTrack.setStatus(OrderStatus.REFUNDED);
		orderTrack.setUpdatedTime(new Date());
		orderTrack.setNotes(OrderStatus.REFUNDED.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(orderTrack);
		
		Order updatedOrder = orderRepository.save(order);
		System.out.println(updatedOrder.getPhoneNumber());
	}
}

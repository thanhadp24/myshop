package com.shopapp;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.product.Product;
import com.shopapp.repository.CartRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestCart {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testSaveItems() {
		CartItem cart = new CartItem();
		Customer customer = entityManager.find(Customer.class, 1);
		Product product = entityManager.find(Product.class, 2);
		cart.setCustomer(customer);
		cart.setProduct(product);
		cart.setQuantity(2);
		cartRepository.save(cart);
	}
	
	@Test
	public void testFindByCustomer() {
		List<CartItem> list = cartRepository.findByCustomer(entityManager.find(Customer.class,1));
		list.forEach(System.out::println);
	}
	
	@Test
	public void updateQuantity() {
		cartRepository.updateQuantity(1, 2, 3);
	}
	
	@Test
	public void deleteQuantity() {
		cartRepository.deleteByCustomerAndProduct(1, 2);
	}
}

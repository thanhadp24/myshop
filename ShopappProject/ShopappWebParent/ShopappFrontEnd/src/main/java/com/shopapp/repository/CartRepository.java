package com.shopapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.product.Product;


@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{
	
	public List<CartItem> findByCustomer(Customer customer);
	
	public CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.customer.id = :customerId "
			+ "AND c.product.id = :productId")
	@Modifying
	public void updateQuantity(Integer customerId, Integer productId, int quantity);
	
	@Query("DELETE FROM CartItem c WHERE c.customer.id = :customerId "
			+ "AND c.product.id = :productId")
	@Modifying
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
}

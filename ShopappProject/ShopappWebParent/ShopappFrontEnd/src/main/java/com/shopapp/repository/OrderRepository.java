package com.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	@Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p "
			+ "WHERE o.customer.id = :customerId  "
			+ "AND (p.name LIKE %:keyword%)")
	public Page<Order> findAll(String keyword, Integer customerId, Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
	public Page<Order> findAll(Integer customerId, Pageable pageable);
	
	public Order findByIdAndCustomer(Integer orderId, Customer customer);
}

package com.shopapp.admin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.order.Order;

@Repository
public interface OrderRepository extends SearchRepository<Order, Integer>{
	
	@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %:keyword% "
			+ "OR CONCAT(o.firstName, ' ', o.lastName) LIKE %:keyword% "
			+ "OR o.firstName LIKE %:keyword% "
			+ "OR o.lastName LIKE %:keyword% OR o.phoneNumber LIKE %:keyword% "
			+ "OR o.addressLine1 LIKE %:keyword% OR o.addressLine2 LIKE %:keyword% "
			+ "OR o.postalCode LIKE %:keyword% OR o.city LIKE %:keyword% "
			+ "OR o.state LIKE %:keyword% OR o.country LIKE %:keyword% "
			+ "OR o.paymentMethod LIKE %:keyword% OR o.orderStatus LIKE %:keyword% "
			+ "OR o.customer.firstName LIKE %:keyword% OR o.customer.lastName LIKE %:keyword% ")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	@Query("SELECT NEW com.shopapp.common.entity.order.Order(o.id, o.orderTime, o.productCost, "
			+ "o.subtotal, o.total) FROM Order o WHERE "
			+ "o.orderTime between ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}

package com.shopapp.admin.repository;

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
}

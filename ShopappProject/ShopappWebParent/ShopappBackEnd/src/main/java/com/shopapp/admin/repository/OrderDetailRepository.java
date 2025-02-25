package com.shopapp.admin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.order.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	
	@Query("SELECT new com.shopapp.common.entity.order.OrderDetail(od.product.category.name, "
			+ " od.quantity, od.shippingCost, od.productCost, od.subtotal) "
			+ " FROM OrderDetail od WHERE od.order.orderTime BETWEEN ?1 AND ?2")
	public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);
	
	@Query("SELECT new com.shopapp.common.entity.order.OrderDetail(od.quantity, "
			+ " od.product.name, od.shippingCost, od.productCost, od.subtotal) "
			+ " FROM OrderDetail od WHERE od.order.orderTime BETWEEN ?1 AND ?2")
	public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime); 
}

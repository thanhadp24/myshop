package com.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.order.OrderDetail;
import com.shopapp.common.enumm.OrderStatus;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

	@Query("SELECT COUNT(od) FROM OrderDetail od JOIN OrderTrack ot ON od.order.id = ot.order.id "
			+ "WHERE od.product.id = ?1 AND od.order.customer.id = ?2 AND "
			+ "ot.status = ?3")
	public Long countByProductAndCustomerAndOrderStatus(Integer productId, Integer customerId,
			OrderStatus orderStatus);
}

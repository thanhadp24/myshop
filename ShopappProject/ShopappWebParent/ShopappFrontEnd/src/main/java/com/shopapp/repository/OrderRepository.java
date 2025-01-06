package com.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

}

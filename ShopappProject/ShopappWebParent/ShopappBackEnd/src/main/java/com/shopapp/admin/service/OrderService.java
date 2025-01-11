package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.exception.OrderNotFoundException;

public interface OrderService {

	void getByPage(int pageNum, PagingAndSortingHelper helper);
	
	Order get(Integer id) throws OrderNotFoundException;
	
	void delete(Integer id) throws OrderNotFoundException, com.shopapp.common.exception.OrderNotFoundException;
	
	List<Country> getAllCountries();

	void save(Order order);

	void updateOrderStatus(Integer orderId, String status);
}

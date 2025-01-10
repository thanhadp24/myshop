package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.exception.OrderNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.order.Order;

public interface OrderService {

	void getByPage(int pageNum, PagingAndSortingHelper helper);
	
	Order get(Integer id) throws OrderNotFoundException;
	
	void delete(Integer id) throws OrderNotFoundException;
	
	List<Country> getAllCountries();

	void save(Order order);
}

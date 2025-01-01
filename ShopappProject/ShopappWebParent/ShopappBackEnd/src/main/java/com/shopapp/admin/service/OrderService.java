package com.shopapp.admin.service;

import com.shopapp.admin.exception.OrderNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.order.Order;

public interface OrderService {

	void getByPage(int pageNum, PagingAndSortingHelper helper);
	
	Order get(Integer id) throws OrderNotFoundException;
	
	void delete(Integer id) throws OrderNotFoundException;
}

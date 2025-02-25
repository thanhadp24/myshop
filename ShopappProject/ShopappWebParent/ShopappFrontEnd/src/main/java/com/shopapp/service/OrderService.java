package com.shopapp.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.bean.OrderReturnRequest;
import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.enumm.PaymentMethod;
import com.shopapp.common.exception.OrderNotFoundException;

public interface OrderService {
	
	Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);

	Page<Order> getForCustomerByPage(int pageNum, Customer customer, String sortField, String sortDir, String keyword);
	
	Order get(Integer id, Customer customer) throws OrderNotFoundException;

	void setOrderReturnRequest(OrderReturnRequest request, Customer customer) throws OrderNotFoundException;
}

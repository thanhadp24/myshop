package com.shopapp.service;

import java.util.List;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.enumm.PaymentMethod;

public interface OrderService {
	
	Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);
}

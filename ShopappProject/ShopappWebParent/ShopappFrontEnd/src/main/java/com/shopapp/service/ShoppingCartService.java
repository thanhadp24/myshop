package com.shopapp.service;

import java.util.List;

import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.exception.ShoppingCartException;

public interface ShoppingCartService {
	
	List<CartItem> getCartItems(Customer customer);
	
	Integer addProduct(Integer productId, int quantity, Customer customer) throws ShoppingCartException;
	
	float updateQuantity(Integer productId, int quantity, Customer customer);
	
	void removeProduct(Integer productId, Customer customer);

	void deleteByCustomer(Customer customer);
}

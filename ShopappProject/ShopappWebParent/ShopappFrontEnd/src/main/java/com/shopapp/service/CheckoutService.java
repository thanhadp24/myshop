package com.shopapp.service;

import java.util.List;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.ShippingRate;

public interface CheckoutService {
	
	CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate);
}

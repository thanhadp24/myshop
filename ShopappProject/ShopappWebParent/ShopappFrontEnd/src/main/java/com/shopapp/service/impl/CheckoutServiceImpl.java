package com.shopapp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.common.Common;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.ShippingRate;
import com.shopapp.common.entity.product.Product;
import com.shopapp.service.CheckoutService;

@Service
public class CheckoutServiceImpl implements CheckoutService{

	@Override
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems);
		float productTotal = calculateProductTotal(cartItems);
		float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
		float paymentTotal = productTotal + shippingCostTotal;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		checkoutInfo.setDeliverDays(shippingRate.getDays());

		return checkoutInfo;
	}

	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0f;
		
		for(CartItem cartItem: cartItems) {
			Product product = cartItem.getProduct();
			float dimWeight = product.getLength()*product.getHeight()
							*product.getWeight() / Common.DIM_DIVISOR;
			float finalWeight = product.getWeight() > dimWeight ? product.getWeight():dimWeight;
			float shippingCost = finalWeight * cartItem.getQuantity() * shippingRate.getRate();
			
			cartItem.setShippingCost(shippingCost);
			
			shippingCostTotal += shippingCost;
		}
		
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
		return cartItems.stream()
				.map(ci -> ci.getSubTotal())
				.reduce(0f, Float::sum);
	}

	private float calculateProductCost(List<CartItem> cartItems) {
		float cost = 0f;
		cost = cartItems.stream()
				.map(ci -> ci.getProduct().getCost() * ci.getQuantity())
				.reduce(cost, Float::sum);
		System.out.println("cost with stream " + cost);	
		cost = 0f;
		for(CartItem cartItem: cartItems) {
			cost += cartItem.getQuantity() + cartItem.getProduct().getCost();
		}
		System.out.println("cost with for " + cost);	
		return cost;
	}
}

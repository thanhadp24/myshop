package com.shopapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.ShippingRate;
import com.shopapp.service.AddressService;
import com.shopapp.service.CustomerService;
import com.shopapp.service.ShippingRateService;
import com.shopapp.service.ShoppingCartService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {
	
	@Autowired	private ShoppingCartService cartService;
	
	@Autowired	private CustomerService customerService;
	
	@Autowired	private ShippingRateService rateService;
	
	@Autowired	private AddressService addressService;
	
	@GetMapping("/cart")
	public String viewCart(HttpServletRequest request, Model model) {
		
		Customer customer = getAuthenticatedCustomer(request);
		
		List<CartItem> cartItems = cartService.getCartItems(customer);
		
		float estimatedTotal = 0.f;
		
		estimatedTotal = cartItems.stream()
				.map(ci -> ci.getSubTotal())
				.reduce(estimatedTotal, Float::sum)
				.floatValue();
		
		Address defaultAddress = addressService.getDefaultByCustomer(customer);
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;
		
		if(defaultAddress != null) {
			shippingRate = rateService.getForAddress(defaultAddress);
		}else {
			usePrimaryAddressAsDefault = true;
			shippingRate = rateService.getForCustomer(customer);
		}
		
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate != null);
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("cartItems", cartItems);
		
		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);
		return customerService.getByEmail(customerEmail);
	}
}

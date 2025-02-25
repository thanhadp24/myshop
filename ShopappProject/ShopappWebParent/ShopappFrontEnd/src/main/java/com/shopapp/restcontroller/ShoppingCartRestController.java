package com.shopapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.common.entity.Customer;
import com.shopapp.common.exception.CustomerNotFoundException;
import com.shopapp.exception.ShoppingCartException;
import com.shopapp.service.CustomerService;
import com.shopapp.service.ShoppingCartService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") int quantity,
			HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
			return updatedQuantity + " item(s) of this product were added to your shopping cart";
		} catch (CustomerNotFoundException e) {
			e.printStackTrace();
			return "You must login to add this product to cart";
		} catch (ShoppingCartException e) {
			return e.getMessage();
		}
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateProductQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") int quantity,
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subTotal = cartService.updateQuantity(productId, quantity, customer);
			return subTotal + "";
		} catch (CustomerNotFoundException e) {
			e.printStackTrace();
			return "You must login to change of product quantity";
		}
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
				HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			return "The product has been removed from your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove this product";
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);

		if(customerEmail == null) {
			throw new CustomerNotFoundException("No authenticated customer!!!");
		}
		
		return customerService.getByEmail(customerEmail);
	}
	
}

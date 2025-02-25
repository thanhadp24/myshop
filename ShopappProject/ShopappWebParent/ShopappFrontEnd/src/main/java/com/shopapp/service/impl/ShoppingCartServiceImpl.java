package com.shopapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.product.Product;
import com.shopapp.exception.ShoppingCartException;
import com.shopapp.repository.CartRepository;
import com.shopapp.repository.ProductRepository;
import com.shopapp.service.ShoppingCartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService{

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public List<CartItem> getCartItems(Customer customer) {
		return cartRepository.findByCustomer(customer);
	}
	
	@Override
	public Integer addProduct(Integer productId, int quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		CartItem cartItem = cartRepository.findByCustomerAndProduct(customer, product);
		if(cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add more " + quantity + " item(s)"
						+ ".Because there's already " + cartItem.getQuantity() + " item(s) in "
						+ "your shopping cart.Maximum allowed quantity is 5");
			}
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updatedQuantity);
		cartRepository.save(cartItem);
		
		return updatedQuantity;
	}
	
	@Override
	public float updateQuantity(Integer productId, int quantity, Customer customer) {
		cartRepository.updateQuantity(customer.getId(), productId, quantity);
		Product product = productRepository.findById(productId).get();
		
		return product.getDiscountPrice() * quantity;
	}
	
	@Override
	public void removeProduct(Integer productId, Customer customer) {
		cartRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	
	@Override
	public void deleteByCustomer(Customer customer) {
		cartRepository.deleteByCustomer(customer.getId());
	}
}

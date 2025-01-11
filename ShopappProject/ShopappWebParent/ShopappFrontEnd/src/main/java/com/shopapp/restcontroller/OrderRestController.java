package com.shopapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.bean.OrderReturnRequest;
import com.shopapp.bean.OrderReturnResponse;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.exception.CustomerNotFoundException;
import com.shopapp.common.exception.OrderNotFoundException;
import com.shopapp.service.CustomerService;
import com.shopapp.service.OrderService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/orders/return")
	public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
			HttpServletRequest servletRequest){
		
		Customer customer = null;
		try {
			customer = getAuthenticatedCustomer(servletRequest);
			
		} catch (CustomerNotFoundException e) {
			return new ResponseEntity<>("Authentication failed", HttpStatus.BAD_REQUEST);
		}
		
		try {
			orderService.setOrderReturnRequest(returnRequest, customer);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);

		if(customerEmail == null) {
			throw new CustomerNotFoundException("No authenticated customer!!!");
		}
		
		return customerService.getByEmail(customerEmail);
	}
}

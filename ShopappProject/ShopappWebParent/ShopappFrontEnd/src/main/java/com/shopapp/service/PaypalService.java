package com.shopapp.service;

import com.shopapp.exception.PaypalApiException;

public interface PaypalService {

	boolean validateOrder(String orderId) throws PaypalApiException;
}

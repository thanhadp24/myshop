package com.shopapp.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shopapp.common.bean.PaymentSettingBag;
import com.shopapp.exception.PaypalApiException;
import com.shopapp.paypal.PaypalOrderResponse;
import com.shopapp.service.PaypalService;
import com.shopapp.service.SettingService;

@Component
public class PaypalServiceImpl implements PaypalService{

	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	
	@Autowired
	private SettingService settingService;
	
	@Override
	public boolean validateOrder(String orderId) throws PaypalApiException {
		PaypalOrderResponse paypalOrderResponse = getOrderDetails(orderId);
		
		return paypalOrderResponse.validate(orderId);
	}

	private PaypalOrderResponse getOrderDetails(String orderId) throws PaypalApiException {
		ResponseEntity<PaypalOrderResponse> response = makeRequest(orderId);
		
		HttpStatus statusCode = (HttpStatus) response.getStatusCode();
		if(!statusCode.equals(HttpStatus.OK)) {
			throwException4NonOK(statusCode);
		}
		
		return response.getBody();
	}

	private ResponseEntity<PaypalOrderResponse> makeRequest(String orderId) {
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String baseUrl = paymentSettings.getPaypalUrl();
		String clientId = paymentSettings.getClientId();
		String clientSecret = paymentSettings.getClientSecret();
		
		String requestURL = baseUrl + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en-US");
		headers.setBasicAuth(clientId, clientSecret);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.exchange
				(requestURL, HttpMethod.GET, request, PaypalOrderResponse.class);
	}

	private void throwException4NonOK(HttpStatus statusCode) throws PaypalApiException {
		String message = null;
		
		switch (statusCode) {
		case NOT_FOUND: {
			message = "Order id not found!";
		}
		case BAD_REQUEST: {
			message = "Bad request to paypal checkout api";
		}
		case INTERNAL_SERVER_ERROR: {
			message = "Paypal server error";
		}
		default:
			message = "Paypal return non-OK status code";
		}
		throw new PaypalApiException(message);
	}
}

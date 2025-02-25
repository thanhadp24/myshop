package com.shopapp;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shopapp.paypal.PaypalOrderResponse;

public class TestPaypalAPI {

	private static final String BASE_URL = "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AcuOeRoHvF4cMiICLQ3_pCgJONof0EwtcOoM4CT7z0MypkGhey59qxIVN9MfA1pdf1qeS88w7VnJoW_0";
	private static final String CLIENT_SECRET = "EInqLcMhpiJCVPWeN1SNKaNL40rnbK75rGzplZ7DCHSk6OlNSrWjWF7UpL41KEZZEn3Yd3VUytjvp4Yc";
	
	@Test
	public void test() {
		String orderId = "88823153HS821034W";
		String requestURL = BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en-US");
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange
				(requestURL, HttpMethod.GET, request, PaypalOrderResponse.class);
		PaypalOrderResponse por = response.getBody();
		System.out.println("orderid " + por.getId());
		System.out.println("validated: " + por.validate(orderId));
	}
}

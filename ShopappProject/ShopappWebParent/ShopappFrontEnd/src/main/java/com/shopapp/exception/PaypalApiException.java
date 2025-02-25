package com.shopapp.exception;

public class PaypalApiException extends Exception{

	private static final long serialVersionUID = -5595738794267787114L;

	public PaypalApiException(String message) {
		super(message);
	}
}

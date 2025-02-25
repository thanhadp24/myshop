package com.shopapp.admin.exception;

public class CustomerNotFoundException extends Exception{

	private static final long serialVersionUID = 4887147943117096955L;

	public CustomerNotFoundException(String message) {
		super(message);
	}
}

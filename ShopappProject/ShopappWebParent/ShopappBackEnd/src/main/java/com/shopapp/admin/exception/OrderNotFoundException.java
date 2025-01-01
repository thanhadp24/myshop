package com.shopapp.admin.exception;

public class OrderNotFoundException extends Exception{

	private static final long serialVersionUID = -2390922186315447190L;

	public OrderNotFoundException(String message) {
		super(message);
	}
}

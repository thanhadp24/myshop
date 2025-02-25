package com.shopapp.common.exception;

public class ProductNotFoundException extends Exception{

	private static final long serialVersionUID = -8580577746498012872L;

	public ProductNotFoundException(String message) {
		super(message);
	}
}

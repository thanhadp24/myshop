package com.shopapp.common.exception;

public class ReviewNotFoundException extends Exception{


	private static final long serialVersionUID = -2285863628762929485L;

	public ReviewNotFoundException(String message) {
		super(message);
	}
}

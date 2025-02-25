package com.shopapp.common.exception;

public class OrderNotFoundException extends Exception{

	private static final long serialVersionUID = 5403325336155950128L;

	public OrderNotFoundException(String message) {
		super(message);
	}
}

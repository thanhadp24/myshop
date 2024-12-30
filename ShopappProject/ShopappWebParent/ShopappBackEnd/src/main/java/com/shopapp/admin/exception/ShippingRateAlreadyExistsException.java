package com.shopapp.admin.exception;

public class ShippingRateAlreadyExistsException extends Exception{


	private static final long serialVersionUID = -5001769297186024977L;

	public ShippingRateAlreadyExistsException(String message) {
		super(message);
	}
}

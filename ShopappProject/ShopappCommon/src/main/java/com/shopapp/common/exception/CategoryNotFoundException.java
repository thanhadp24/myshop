package com.shopapp.common.exception;

public class CategoryNotFoundException extends Exception{

	private static final long serialVersionUID = 1351479639309845381L;

	public CategoryNotFoundException(String message) {
		super(message);
	}
}

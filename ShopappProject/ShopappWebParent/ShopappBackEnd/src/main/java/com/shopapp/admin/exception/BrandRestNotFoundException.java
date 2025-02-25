package com.shopapp.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Brand not found")
public class BrandRestNotFoundException extends Exception {

	private static final long serialVersionUID = -6987771300342196518L;

}

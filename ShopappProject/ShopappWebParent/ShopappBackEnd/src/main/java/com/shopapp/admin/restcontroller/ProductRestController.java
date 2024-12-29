package com.shopapp.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.service.ProductService;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(Integer id, String name) {
		
		return productService.checkUnique(name, id);
	}
}

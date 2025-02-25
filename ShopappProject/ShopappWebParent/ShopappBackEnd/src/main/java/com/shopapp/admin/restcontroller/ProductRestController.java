package com.shopapp.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.service.ProductService;
import com.shopapp.common.dto.ProductDto;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.exception.ProductNotFoundException;

@RestController
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/products/check_unique")
	public String checkUnique(Integer id, String name) {
		
		return productService.checkUnique(name, id);
	}
	
	@GetMapping("/products/get/{id}")
	public ProductDto getProduct(@PathVariable("id") Integer id) throws ProductNotFoundException {
		Product product = productService.get(id);
		return new ProductDto(product.getName(), 
				product.getMainImagePath(), product.getPrice(), product.getCost());
	}
}

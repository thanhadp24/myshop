package com.shopapp.service;

import org.springframework.data.domain.Page;

import com.shopapp.common.entity.Product;
import com.shopapp.common.exception.ProductNotFoundException;

public interface ProductService {
	
	Page<Product> getByPage(int pageNum, Integer categoryId); 
	
	Product getByAlias(String alias) throws ProductNotFoundException;
	
	Page<Product> search(String keyword, int pageNum);
}

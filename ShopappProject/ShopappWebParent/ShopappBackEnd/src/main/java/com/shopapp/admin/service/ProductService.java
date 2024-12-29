package com.shopapp.admin.service;

import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.Product;
import com.shopapp.common.exception.ProductNotFoundException;

public interface ProductService {
	void getByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId);

	Product get(Integer id) throws ProductNotFoundException;

	Product save(Product product);
	
	void saveProductPrice(Product productInForm);
	
	String checkUnique(String name, Integer id);
	
	void delete(Integer id) throws ProductNotFoundException;
	
	void updateEnabled(Integer id, boolean status);

}

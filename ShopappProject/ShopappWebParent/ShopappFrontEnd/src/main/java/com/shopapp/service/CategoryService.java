package com.shopapp.service;

import java.util.List;

import com.shopapp.common.entity.Category;
import com.shopapp.common.exception.CategoryNotFoundException;

public interface CategoryService {
	
	List<Category> getAllWithNoChildrens();
	
	Category getCategory(String alias) throws CategoryNotFoundException;
	
	List<Category> getParents(Category children);
}

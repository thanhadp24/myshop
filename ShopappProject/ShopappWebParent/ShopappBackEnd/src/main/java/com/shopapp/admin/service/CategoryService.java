package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.bean.CategoryPageInfo;
import com.shopapp.common.entity.Category;
import com.shopapp.common.exception.CategoryNotFoundException;

public interface CategoryService {
	
	List<Category> categoryByPage(int pageNum, String sortDir, CategoryPageInfo categoryPageInfo, String keyword);
	
	List<Category> getCategoryUsedInForm();
	
	Category save(Category category);
	
	Category get(int id) throws CategoryNotFoundException;
	
	void delete(int id) throws CategoryNotFoundException;
	
	String checkUnique(Integer id, String name, String alias);

	void updateEnabledStatus(Integer id, boolean enable);
	
}

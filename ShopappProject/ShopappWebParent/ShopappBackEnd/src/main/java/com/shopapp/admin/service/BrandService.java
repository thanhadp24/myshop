package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.exception.BrandNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.Brand;

public interface BrandService {
	
	List<Brand> getAll();
	
	void getByPage(int pageNum, PagingAndSortingHelper helper);
	
	Brand save(Brand brand);
	
	Brand get(Integer id) throws BrandNotFoundException;
	
	void delete(Integer id) throws BrandNotFoundException;
	
	String checkUnique(Integer id, String name);

}

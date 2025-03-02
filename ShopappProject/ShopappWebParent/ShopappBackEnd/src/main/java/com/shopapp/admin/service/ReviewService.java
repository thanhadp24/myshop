package com.shopapp.admin.service;

import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.Review;
import com.shopapp.common.exception.ReviewNotFoundException;

public interface ReviewService {

	void getByPage(int pageNum, PagingAndSortingHelper helper);
	
	Review get(Integer id) throws ReviewNotFoundException;
	
	void save(Review reviewInForm);

	void delete(Integer id) throws ReviewNotFoundException;
}

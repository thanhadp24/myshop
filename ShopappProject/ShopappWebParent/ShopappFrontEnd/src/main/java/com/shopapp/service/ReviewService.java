package com.shopapp.service;

import org.springframework.data.domain.Page;

import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.exception.ReviewNotFoundException;

public interface ReviewService {

	Page<Review> getByPage(Customer customer, String keyword, int pageNum, String sortField, String sortDir);

	Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException;
}

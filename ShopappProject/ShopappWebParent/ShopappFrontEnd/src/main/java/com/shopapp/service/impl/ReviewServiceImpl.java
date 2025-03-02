package com.shopapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopapp.common.Common;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.exception.ReviewNotFoundException;
import com.shopapp.repository.ReviewRepository;
import com.shopapp.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Override
	public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException {
		Review review = reviewRepository.findByCustomerAndId(customer.getId(), reviewId);
		if(review == null) {
			throw new ReviewNotFoundException("Could not find any review with ID " + reviewId);
		}
		return review;
	}
	
	@Override
	public Page<Review> getByPage(Customer customer, String keyword, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc")? sort.ascending(): sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum-1, Common.REVIEWS_PER_PAGE, sort);
		if(keyword != null) {
			return reviewRepository.findByCustomer(customer.getId(), keyword, pageable);
		}
		return reviewRepository.findByCustomer(customer.getId(), pageable);
	}
}

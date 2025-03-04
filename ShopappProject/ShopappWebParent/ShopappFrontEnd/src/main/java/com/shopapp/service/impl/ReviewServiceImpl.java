package com.shopapp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopapp.common.Common;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.enumm.OrderStatus;
import com.shopapp.common.exception.ReviewNotFoundException;
import com.shopapp.repository.OrderDetailRepository;
import com.shopapp.repository.ProductRepository;
import com.shopapp.repository.ReviewRepository;
import com.shopapp.service.ReviewService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private OrderDetailRepository detailRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
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
	
	@Override
	public Page<Review> get3MostRecentReviewsByProduct(Product product){
		Sort sort = Sort.by("reviewTime").descending();
		Pageable pageable = PageRequest.of(0, 3, sort);
		
		return reviewRepository.findByProduct(product, pageable);
	}
	
	@Override
	public Page<Review> getByProduct(Product product, int pageNum, String sortDir, String sortField){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending(): sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, Common.REVIEWS_PER_PAGE, sort);
		return reviewRepository.findByProduct(product, pageable);
	}
	
	@Override
	public boolean didCustomerReviewProduct(Customer customer, Integer productId) {
		Long count = reviewRepository.countByProductAndCustomer(productId, customer.getId());
		return count > 0;
	}
	
	@Override
	public boolean canCustomerReviewProduct(Customer customer, Integer productId) {
		Long count = detailRepository.countByProductAndCustomerAndOrderStatus(productId, customer.getId(), OrderStatus.DELIVERED);
		System.out.println(">> check count " + count);
		return count > 0;
	}
	
	@Override
	public Review save(Review review) {
		review.setReviewTime(new Date());
		Review savedReview = reviewRepository.save(review);
		
		productRepository.updateReviewCountAndAverageRating(review.getProduct().getId());
		
		return savedReview;
	}
}

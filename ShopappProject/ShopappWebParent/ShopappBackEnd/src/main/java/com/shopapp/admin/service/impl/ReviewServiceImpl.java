package com.shopapp.admin.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.repository.ProductRepository;
import com.shopapp.admin.repository.ReviewRepository;
import com.shopapp.admin.service.ReviewService;
import com.shopapp.common.entity.Review;
import com.shopapp.common.exception.ReviewNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Review get(Integer id) throws ReviewNotFoundException {
		try {
			return reviewRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ReviewNotFoundException("Could not find review with id: " + id);
		}
	}

	@Override
	public void getByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, Common.REVIEWS_PER_PAGE, reviewRepository);
	}

	@Override
	public void save(Review reviewInForm) {
		Review reviewInDb = reviewRepository.findById(reviewInForm.getId()).get();
		reviewInDb.setComment(reviewInForm.getComment());
		reviewInDb.setHeadline(reviewInForm.getHeadline());
		
		reviewRepository.save(reviewInDb);
		productRepository.updateReviewCountAndAverageRating(reviewInDb.getProduct().getId());
	}
	
	@Override
	public void delete(Integer id) throws ReviewNotFoundException {
		if(!reviewRepository.existsById(id)) {
			throw new ReviewNotFoundException("Could not find review with id: " + id);
		}
		reviewRepository.deleteById(id);
	}
}

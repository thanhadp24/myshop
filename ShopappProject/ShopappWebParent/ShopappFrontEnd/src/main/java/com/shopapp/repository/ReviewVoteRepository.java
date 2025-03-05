package com.shopapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.ReviewVote;

@Repository
public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Integer>{

	@Query("SELECT r FROM ReviewVote r WHERE r.review.id = ?1 AND r.customer.id = ?2")
	public ReviewVote findByReviewAndCustomer(Integer reviewId, Integer customerId);
	
	@Query("SELECT r FROM ReviewVote r WHERE r.review.product.id = ?1 AND r.customer.id = ?2")
	public List<ReviewVote> findByProductAndCustomer(Integer productId, Integer customerId);
}

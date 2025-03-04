package com.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.product.Product;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1")
	public Page<Review> findByCustomer(Integer customerId, Pageable pageable);
	
	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND "
			+ "(r.headline LIKE %?2% OR r.comment LIKE %?2% OR "
			+ "r.product.name LIKE %?2%)")
	public Page<Review> findByCustomer(Integer customerId, String keyword, Pageable pageable);
	
	@Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND r.id = ?2")
	public Review findByCustomerAndId(Integer customerId, Integer reviewId);
	
	public Page<Review> findByProduct(Product product, Pageable pageable); 
	
	@Query("SELECT COUNT(*) FROM Review r WHERE r.customer.id = ?2 AND "
			+ "r.product.id = ?1")
	public Long countByProductAndCustomer(Integer productId, Integer customerId);
}

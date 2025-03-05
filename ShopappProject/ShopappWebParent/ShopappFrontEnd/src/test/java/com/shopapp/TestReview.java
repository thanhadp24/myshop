package com.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.product.Product;
import com.shopapp.repository.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestReview {

	
	@Autowired
	private ReviewRepository repository;
	
	@Test
	public void findByCustomerNoKeyword() {
		Pageable pageable = PageRequest.of(1, 10);
		repository.findByCustomer(3, pageable).forEach(System.out::println);
	}
	
	@Test
	public void findByCustomerWithKeyword() {
		Pageable pageable = PageRequest.of(0, 10);
		repository.findByCustomer(1, "ao dai", pageable).forEach(System.out::println);
	}
	
	@Test
	public void findByCustomerAndId() {
		Pageable pageable = PageRequest.of(0, 10);
		Review review = repository.findByCustomerAndId(1, 3);
		System.out.println(review);
	}
	
	@Test
	public void testFindByProduct() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Review> page = repository.findByProduct(new Product(1), pageable);
		page.getContent().forEach(System.out::println);
	}
	
	@Test
	public void testCount() {
		Integer productId = 1;
		Integer customerId = 3;
		
		Long countByProductAndCustomer = repository.countByProductAndCustomer(productId, customerId);
		System.out.println(countByProductAndCustomer);
	}
	
	@Test
	public void testVote() {
		repository.updateVoteCount(9);
	}
	
	@Test
	public void testGetReviewVote() {
		Integer voteCount = repository.getVoteCount(9);
		System.out.println(voteCount);
	}
}

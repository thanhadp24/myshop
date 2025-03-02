package com.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.entity.Review;
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
}

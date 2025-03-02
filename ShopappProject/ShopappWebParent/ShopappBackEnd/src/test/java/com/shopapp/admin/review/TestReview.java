package com.shopapp.admin.review;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.ReviewRepository;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestReview {

	@Autowired private ReviewRepository repository;
	
	@Test
	public void testCreateView() {
		Integer productId = 1;
		Product product = new Product(productId);
		
		Integer customerId = 1;
		Customer customer = new Customer(customerId);
		
		Review review = new Review();
		review.setProduct(product);
		review.setCustomer(customer);
		review.setHeadline("good product");
		review.setComment("nice product with cheap price");
		review.setReviewTime(new Date());
		review.setRating(4);
		
		repository.save(review);
	}
	
	@Test
	public void listReview() {
		repository.findAll().forEach(System.out::println);
	}
	
	@Test
	public void getReviewById() {
		Review review = repository.findById(1).get();
		System.out.println(review);
	}
	
	@Test
	public void updateReview() {
		Review review = repository.findById(1).get();
		review.setHeadline("test review");
		review.setComment("awesome product");
		repository.save(review);
	}
	
	@Test
	public void deleteReview() {
		repository.deleteById(1);
	}
}

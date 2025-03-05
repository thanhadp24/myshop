package com.shopapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.ReviewVote;
import com.shopapp.repository.ReviewVoteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestReviewVote {

	
	@Autowired
	private ReviewVoteRepository repository;
	
	@Test
	public void testSaveVote() {
		ReviewVote reviewVote = new ReviewVote();
		reviewVote.setCustomer(new Customer(3));
		reviewVote.setReview(new Review(9));
		reviewVote.voteUp();
		
		ReviewVote save = repository.save(reviewVote);
		assertThat(save.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByReviewAndCustomer() {
		ReviewVote reviewVote = repository.findByReviewAndCustomer(9, 3);
		System.out.println(reviewVote);
	}
}

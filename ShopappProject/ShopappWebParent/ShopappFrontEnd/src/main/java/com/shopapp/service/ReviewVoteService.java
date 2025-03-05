package com.shopapp.service;

import java.util.List;

import com.shopapp.bean.VoteResult;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.ReviewVote;
import com.shopapp.enumm.VoteType;

public interface ReviewVoteService {

	VoteResult unDoVote(ReviewVote reviewVote, Integer reviewId, VoteType voteType);

	VoteResult doVote(Integer reviewId, Customer customer, VoteType voteType);

	void markReviewVoted4ProductByCustomer(List<Review> reviews, Integer productId, Integer customerId);

	
}

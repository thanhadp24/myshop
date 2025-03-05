package com.shopapp.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.bean.VoteResult;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.ReviewVote;
import com.shopapp.enumm.VoteType;
import com.shopapp.repository.ReviewRepository;
import com.shopapp.repository.ReviewVoteRepository;
import com.shopapp.service.ReviewVoteService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewVoteServiceImpl implements ReviewVoteService{
	
	@Autowired private ReviewVoteRepository reviewVoteRepository;
	@Autowired private ReviewRepository reviewRepository;
	
	@Override
	public VoteResult unDoVote(ReviewVote reviewVote, Integer reviewId, VoteType voteType) {
		reviewVoteRepository.delete(reviewVote);
		reviewRepository.updateVoteCount(reviewId);
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		return VoteResult.success("You have unvoted " + voteType + " that review.", voteCount);
	}
	
	@Override
	public VoteResult doVote(Integer reviewId, Customer customer, VoteType voteType) {
		Review review = null;
		try {
			review = reviewRepository.findById(reviewId).get();
		} catch (NoSuchElementException e) {
			return VoteResult.fail("The review ID " + reviewId + " no longer exists");
		}
		
		ReviewVote reviewVote = reviewVoteRepository.findByReviewAndCustomer(reviewId, customer.getId());

		if(reviewVote != null) {
			if(reviewVote.isUpVoted() && voteType.equals(VoteType.UP) ||
					reviewVote.isDownVoted() && voteType.equals(VoteType.DOWN)) {
				return unDoVote(reviewVote, reviewId, voteType);
			}else if(reviewVote.isUpVoted() && voteType.equals(VoteType.DOWN)) {
				reviewVote.voteDown();
			}else if(reviewVote.isDownVoted() && voteType.equals(VoteType.UP)) {
				reviewVote.voteUp();
			}
		}else {
			reviewVote = new ReviewVote();
			reviewVote.setCustomer(customer);
			reviewVote.setReview(review);
			if(voteType.equals(VoteType.UP)) {
				reviewVote.voteUp();
			}else {
				reviewVote.voteDown();
			}
		}
		
		reviewVoteRepository.save(reviewVote);
		reviewRepository.updateVoteCount(reviewId);
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		
		return VoteResult.success("You have successfully voted " + voteType + " that review.", voteCount);
	}
	
	@Override
	public void markReviewVoted4ProductByCustomer(List<Review> reviews, Integer productId, 
			Integer customerId) {
		List<ReviewVote> reviewVotes = reviewVoteRepository.findByProductAndCustomer(productId, customerId);
		
		for(ReviewVote reviewVote: reviewVotes) {
			Review votedReview = reviewVote.getReview();
			if(reviews.contains(votedReview)) {
				int idx = reviews.indexOf(votedReview);
				Review review = reviews.get(idx);
				
				if(reviewVote.isUpVoted()) {
					review.setUpvotedByCurrentCustomer(true);
				}else if(reviewVote.isDownVoted()) {
					review.setDownVotedByCurrentCustomer(true);
				}
			}
		}
	}
}

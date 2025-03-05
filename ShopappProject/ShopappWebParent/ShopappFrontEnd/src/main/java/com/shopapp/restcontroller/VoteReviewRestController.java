package com.shopapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.ControllerHelper;
import com.shopapp.bean.VoteResult;
import com.shopapp.common.entity.Customer;
import com.shopapp.enumm.VoteType;
import com.shopapp.service.ReviewVoteService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class VoteReviewRestController {

	@Autowired
	private ReviewVoteService voteService;
	
	@Autowired
	private ControllerHelper controllerHelper;
	
	@PostMapping("/vote_review/{id}/{type}")
	public VoteResult voteReview(@PathVariable("id") Integer id, 
			@PathVariable("type") String type, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		if(customer == null) {
			return VoteResult.fail("You must login to vote this review");
		}
		
		VoteType voteType = VoteType.valueOf(type.toUpperCase());
		
		return voteService.doVote(id, customer, voteType);
	}
}

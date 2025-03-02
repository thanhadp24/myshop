package com.shopapp.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.paging.PagingAndSortingParam;
import com.shopapp.admin.service.ReviewService;
import com.shopapp.common.entity.Review;
import com.shopapp.common.exception.ReviewNotFoundException;

@Controller
public class ReviewController {

	private String defaultRedirectURL = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/reviews")
	public String viewReviews() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/reviews/page/{pageNum}")
	public String viewReviewByPage(@PathVariable("pageNum") Integer pageNum,
			@PagingAndSortingParam(listName = "reviews", moduleURL = "/reviews") PagingAndSortingHelper helper) {
		
		reviewService.getByPage(pageNum, helper);
		return "reviews/reviews";
	}
	
	@GetMapping("/reviews/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			Review review = reviewService.get(id);
			model.addAttribute("review", review);
			
			return "reviews/review_detail_modal";
		} catch (com.shopapp.common.exception.ReviewNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@GetMapping("/reviews/edit/{id}")
	public String editReview(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			Review review = reviewService.get(id);
			model.addAttribute("review", review);
			model.addAttribute("pageTitle", "Edit review (ID: " + id + ")");
			return "reviews/review_form";
		} catch (ReviewNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@PostMapping("/reviews/save")
	public String saveReview(Review reviewInForm, RedirectAttributes ra) {
		
		reviewService.save(reviewInForm);
		ra.addFlashAttribute("message", "The review has been saved successfully");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/reviews/delete/{id}")
	public String deleteReview(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		try {
			reviewService.delete(id);
			ra.addFlashAttribute("message", "The review has been deleted successfully");
		} catch (ReviewNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}
}

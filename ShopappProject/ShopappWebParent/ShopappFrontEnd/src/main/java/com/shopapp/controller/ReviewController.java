package com.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.common.Common;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.service.CustomerService;
import com.shopapp.service.ReviewService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReviewController {
	
	private String defaultRedirectURL = "redirect:/reviews/page/1?sortDir=desc&sortField=reviewTime";
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/reviews")
	public String viewReviews() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/reviews/page/{pageNum}")
	public String viewReviewByPage(@PathVariable("pageNum") Integer pageNum, Model model,
			HttpServletRequest request, String keyword, String sortField, String sortDir) {
		
		Customer customer = getAuthenticatedCustomer(request);
		Page<Review> page = reviewService.getByPage(customer, keyword, pageNum, sortField, sortDir);
		long totalItems = page.getTotalElements();
		
		long startCount = (pageNum - 1) * Common.ORDERS_PER_PAGE + 1;
		long endCount = startCount + Common.ORDERS_PER_PAGE - 1;
		
		if(endCount > totalItems) {
			endCount = totalItems;
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("reviews", page.getContent());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc":"asc");
		model.addAttribute("moduleURL", "/reviews");
		
		return "reviews/reviews_customer";
	}
	
	@GetMapping("/reviews/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, Model model, 
			HttpServletRequest request, RedirectAttributes ra) {
		Customer customer = getAuthenticatedCustomer(request);
		try {
			Review review = reviewService.getByCustomerAndId(customer, id);
			model.addAttribute("review", review);
			
			return "reviews/review_detail_modal";
		} catch (com.shopapp.common.exception.ReviewNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);
		return customerService.getByEmail(customerEmail);
	}
}	

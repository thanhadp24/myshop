package com.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.ControllerHelper;
import com.shopapp.common.Common;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.Review;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.exception.ProductNotFoundException;
import com.shopapp.service.ProductService;
import com.shopapp.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReviewController {

	private String defaultRedirectURL = "redirect:/reviews/page/1?sortDir=desc&sortField=reviewTime";

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ControllerHelper controllerHelper;

	@GetMapping("/reviews")
	public String viewReviews() {
		return defaultRedirectURL;
	}

	@GetMapping("/ratings/{productAlias}")
	public String viewReviewProduct(@PathVariable("productAlias") String productAlias, Model model) {
		return viewReviewsProductByPage(model, productAlias, 1, "desc", "reviewTime");
	}
	
	@GetMapping("/ratings/{productAlias}/page/{pageNum}")
	public String viewReviewsProductByPage(Model model, @PathVariable("productAlias") String productAlias,
			@PathVariable("pageNum") Integer pageNum, String sortDir, String sortField) {

		Product product = null;
		try {
			product = productService.getByAlias(productAlias);
		} catch (Exception e) {
			return "error/404";
		}

		Page<Review> page = reviewService.getByProduct(product, pageNum, sortDir, sortField);
		long totalItems = page.getTotalElements();

		long startCount = (pageNum - 1) * Common.REVIEWS_PER_PAGE + 1;
		long endCount = startCount + Common.REVIEWS_PER_PAGE - 1;

		if (endCount > totalItems) {
			endCount = totalItems;
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("reviews", page.getContent());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/reviews");
		model.addAttribute("product", product);
		
		model.addAttribute("pageTitle", "Review for " + product.getShortName());

		return "reviews/reviews_product";

	}

	@GetMapping("/reviews/page/{pageNum}")
	public String viewReviewByPage(@PathVariable("pageNum") Integer pageNum, Model model, HttpServletRequest request,
			String keyword, String sortField, String sortDir) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		Page<Review> page = reviewService.getByPage(customer, keyword, pageNum, sortField, sortDir);
		long totalItems = page.getTotalElements();

		long startCount = (pageNum - 1) * Common.REVIEWS_PER_PAGE + 1;
		long endCount = startCount + Common.REVIEWS_PER_PAGE - 1;

		if (endCount > totalItems) {
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
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/reviews");

		System.out.println(">> check page: " + pageNum + ", " + sortDir + ", " + sortField);
		
		return "reviews/reviews_customer";
	}

	@GetMapping("/reviews/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes ra) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		try {
			Review review = reviewService.getByCustomerAndId(customer, id);
			model.addAttribute("review", review);

			return "reviews/review_detail_modal";
		} catch (com.shopapp.common.exception.ReviewNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@PostMapping("/post_review")
	public String postReview(Review review, Model model, HttpServletRequest request,
			Integer productId) {
		
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		Product product = null;
		try {
			product = productService.get(productId);
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		
		review.setCustomer(customer);
		review.setProduct(product);
		
		Review savedReview = reviewService.save(review);
		model.addAttribute("review", savedReview);
		model.addAttribute("pageTitle", "Reviewed for " + product.getShortName());
		
		return "reviews/review_done";
	}
	
	@GetMapping("/write_review/product/{productId}")
	public String writeReview(@PathVariable("productId") Integer productId, Model model,
			HttpServletRequest request) {
		
		Review review = new Review();
		Product product = null;
		try {
			product = productService.get(productId);
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
		
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		boolean customerReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());
		
		if(customerReviewed) {
			model.addAttribute("customerReviewed", customerReviewed);
		}else {
			boolean customerCanReview = reviewService.canCustomerReviewProduct(customer, product.getId());
			if(customerCanReview) {
				model.addAttribute("customerCanReview", customerCanReview);
			}else {
				model.addAttribute("noReviewPermission", true);
			}
		}
		
		model.addAttribute("customerReviewed", customerReviewed);
		model.addAttribute("product", product);
		model.addAttribute("review", review);
		model.addAttribute("pageTitle", "Review for " + product.getShortName());
		
		return "reviews/review_form";
	}

	
}

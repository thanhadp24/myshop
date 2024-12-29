package com.shopapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopapp.common.Common;
import com.shopapp.common.entity.Category;
import com.shopapp.common.entity.Product;
import com.shopapp.common.exception.CategoryNotFoundException;
import com.shopapp.common.exception.ProductNotFoundException;
import com.shopapp.service.CategoryService;
import com.shopapp.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired 
	private CategoryService categoryService; 
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/c/{category_alias}")
	public String viewProductsFirstPage(
			@PathVariable("category_alias") String categoryAlias,
			Model model) {
		return viewProducts(categoryAlias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewProducts(
			@PathVariable("category_alias") String categoryAlias,
			@PathVariable("pageNum") Integer pageNum,
			Model model) {
		
		try {
			Category category = categoryService.getCategory(categoryAlias);

			long startCount = (pageNum - 1) * Common.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + Common.PRODUCTS_PER_PAGE - 1;
			
			Page<Product> productPages = productService.getByPage(pageNum, category.getId());
			
			if(endCount > productPages.getTotalElements()) {
				endCount = productPages.getTotalElements();
			}
			
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", productPages.getTotalPages());
			model.addAttribute("totalItems", productPages.getTotalElements());
			model.addAttribute("products", productPages.getContent());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("categoryParents", categoryService.getParents(category));
			model.addAttribute("category", category);
			
			return "product/products_by_category";
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias,
			Model model) {
		
		try {
			Product product = productService.getByAlias(alias);
			model.addAttribute("categoryParents", categoryService.getParents(product.getCategory()));
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());
			
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String viewSearchFirstPage(String keyword, Model model) {
		return viewProductSearch(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String viewProductSearch(String keyword,
			@PathVariable("pageNum") Integer pageNum,
			Model model) {
		
		long startCount = (pageNum - 1) * Common.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + Common.PRODUCTS_PER_PAGE - 1;
		Page<Product> productPages = productService.search(keyword, pageNum);
		
		if(endCount > productPages.getTotalElements()) {
			endCount = productPages.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", productPages.getTotalPages());
		model.addAttribute("totalItems", productPages.getTotalElements());
		model.addAttribute("products", productPages.getContent());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("pageTitle", keyword + " - Search");
		model.addAttribute("keyword", keyword);
		
		return "product/search_result";
	}
	
}

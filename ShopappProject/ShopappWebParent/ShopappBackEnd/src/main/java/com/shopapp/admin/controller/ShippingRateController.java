package com.shopapp.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.exception.ShippingRateAlreadyExistsException;
import com.shopapp.admin.exception.ShippingRateNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.paging.PagingAndSortingParam;
import com.shopapp.admin.service.ShippingRateService;
import com.shopapp.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	
	private String defaultRedirectUrl = "redirect:/shipping_rates/page/1?sortField=country&"
			+ "sortDir=asc";
	
	@Autowired
	private ShippingRateService rateService;
	
	@GetMapping("/shipping_rates")
	public String viewFirstPage() {
		return defaultRedirectUrl;
	}
	
	@GetMapping("shipping_rates/page/{pageNum}")
	public String viewShippingRates(@PagingAndSortingParam(listName = "shippingRates", 
				moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
				@PathVariable("pageNum") int pageNum,
				String keyword, Model model) {
		rateService.getByPage(helper, pageNum);
		return "shipping_rates/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/new")
	public String createNew(Model model) {
		
		model.addAttribute("countries", rateService.getAllCountries());
		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("pageTitle", "Create new Shipping rate");
		
		return "shipping_rates/shipping_rate_form";
	}
	
	@PostMapping("/shipping_rates/save")
	public String saveShippingRate(ShippingRate rate, RedirectAttributes ra) {
		
		try {
			rateService.save(rate);
			ra.addFlashAttribute("message", "The Shipping Rate has been saved successfully");
		} catch (ShippingRateAlreadyExistsException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectUrl;
	}
	
	@GetMapping("/shipping_rates/edit/{id}")
	public String editShippingRate(@PathVariable("id") Integer id,
			Model model, RedirectAttributes ra) {
		
		try {
			ShippingRate shippingRate = rateService.get(id);
			model.addAttribute("countries", rateService.getAllCountries());
			model.addAttribute("rate", shippingRate);
			model.addAttribute("pageTitle", "Edit shipping rate (ID:" + id + ")");
			
			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException e) {
			e.printStackTrace();
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectUrl;
		}
	}
	
	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCodSupported(@PathVariable("id") Integer id,
			@PathVariable("supported") boolean supported, 
			RedirectAttributes ra) {
		
		try {
			System.out.println("supported: " + supported);
			rateService.updateCodSupported(id, supported);
			ra.addFlashAttribute("message", "COD support for shipping rate " + id + " has been updated");
		} catch (ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return defaultRedirectUrl;
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String updateCodSupported(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		try {
			rateService.delete(id);
			ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted");
		} catch (ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return defaultRedirectUrl;
	}
	
}

package com.shopapp.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.exception.CustomerNotFoundException;
import com.shopapp.admin.repository.CountryRepository;
import com.shopapp.admin.service.CustomerService;
import com.shopapp.common.entity.Customer;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@GetMapping("/customers")
	public String viewFirtPage(Model model) {
		return viewByPage(1, "asc", "firstName", null, model);
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String viewByPage(@PathVariable("pageNum") int pageNum,
				String sortDir,
				String sortField,
				String keyword,
				Model model) {
		
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Page<Customer> customerPages = customerService.getByPage(pageNum, keyword, sortDir, sortField);

		String reverseSortDir = (sortDir.equals("asc") ? "desc" : "asc");
		long startCount = (pageNum - 1) * Common.BRANDS_PER_PAGE + 1;
		long endCount = startCount + Common.BRANDS_PER_PAGE - 1;
		
		if(endCount > customerPages.getTotalElements()) {
			endCount = customerPages.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", customerPages.getTotalPages());
		model.addAttribute("totalItems", customerPages.getTotalElements());
		model.addAttribute("customers", customerPages.getContent());
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("moduleURL", "/customers");
		
		model.addAttribute("pageTitle", "Create new customer");
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateEnable(@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enable,
			RedirectAttributes ra) {
		
		customerService.updateEnabledStatus(id, enable);
		String status = enable ? "enabled" : "disabled";
		
		ra.addFlashAttribute("message", "Customer has been " + status);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, 
			Model model, RedirectAttributes ra) {
		
		try {
			Customer customer = customerService.get(id);
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect/customers";
		}
		
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			Customer customer = customerService.get(id);
			
			model.addAttribute("countries", countryRepository.findAllByOrderByNameAsc());
			
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit customer(ID: " + id + ")");
			
			return "customers/customer_form";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		customerService.save(customer);
		ra.addFlashAttribute("message", "Customer " + customer.getFullName() + " has been saved");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			customerService.delete(id);
			ra.addFlashAttribute("message", "Customer with id " + id + " has been deleted");
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/customers";
	}
}

package com.shopapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;
import com.shopapp.service.AddressService;
import com.shopapp.service.CustomerService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AddressController {

	@Autowired 
	private AddressService addressService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		
		List<Address> addresses = addressService.getAddressesBook(customer);
		boolean usePrimaryAddressAsDefault = true;
		
		for(Address address: addresses) {
			if(address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}
		
		model.addAttribute("addresses", addresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		
		return "address_book/addresses";
	}
	
	@GetMapping("/address_book/new")
	public String createAddress(Model model) {
		
		model.addAttribute("countries", customerService.getAllCountries());
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Create new address");
		
		return "address_book/address_form";
	}
	
	@PostMapping("/address_book/save")
	public String saveAddress(Address address, HttpServletRequest request, 
			RedirectAttributes ra) {
		
		Customer customer = getAuthenticatedCustomer(request);
		address.setCustomer(customer);
		addressService.save(address);
		
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if("checkout".equals(redirectOption)) {
			redirectURL += "?redirect=checkout";
		}
				
		
		ra.addFlashAttribute("message", "The address has been saved successfully");
		
		return redirectURL;
	}
	
	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable("id") Integer id, 
			HttpServletRequest request, Model model) {
		
		Customer customer = getAuthenticatedCustomer(request);
		
		Address address = addressService.get(customer.getId(), id);
		
		model.addAttribute("countries", customerService.getAllCountries());
		model.addAttribute("address", address);
		model.addAttribute("pageTitle", "Edit Address (ID:"+ id + ")");
		
		return "address_book/address_form";
	}
	
	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer id, 
			HttpServletRequest request, RedirectAttributes ra) {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.delete(customer.getId(), id);
		
		ra.addFlashAttribute("message", "The address has been deleted successfully");
		
		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer id, 
			HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(id, customer.getId());
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		}else if("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/checkout";
		}
				
		return redirectURL;
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);
		return customerService.getByEmail(customerEmail);
	}
}

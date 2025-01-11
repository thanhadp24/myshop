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
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.exception.OrderNotFoundException;
import com.shopapp.service.CustomerService;
import com.shopapp.service.OrderService;
import com.shopapp.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/orders")
	public String viewFirstPage(HttpServletRequest request, Model model) {
		return getOrdersByPage("desc", "orderTime", null, request, 1, model);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String getOrdersByPage(String sortDir, String sortField, String orderKeyword,
			HttpServletRequest request, @PathVariable("pageNum") Integer pageNum, 
			Model model) {
		
		Customer customer = getAuthenticatedCustomer(request);
		Page<Order> page = orderService.getForCustomerByPage(pageNum, customer, sortField, sortDir, orderKeyword);
		long totalItems = page.getTotalElements();
		
		long startCount = (pageNum - 1) * Common.ORDERS_PER_PAGE + 1;
		long endCount = startCount + Common.ORDERS_PER_PAGE - 1;
		
		if(endCount > totalItems) {
			endCount = totalItems;
		}
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("orders", page.getContent());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("orderKeyword", orderKeyword);
		model.addAttribute("moduleURL", "/orders");
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc": "asc");
		
		model.addAttribute("endcount", endCount);
		
		return "orders/orders_customer";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetail(@PathVariable("id") Integer id, Model model, 
			HttpServletRequest request, RedirectAttributes ra) {
		try {
			Order order = orderService.get(id, getAuthenticatedCustomer(request));
			model.addAttribute("order", order);
			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/orders";
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);
		return customerService.getByEmail(customerEmail);
	}
}

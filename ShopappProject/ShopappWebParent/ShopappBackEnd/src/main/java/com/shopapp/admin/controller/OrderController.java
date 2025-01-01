package com.shopapp.admin.controller;

import java.util.List;

import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.exception.OrderNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.paging.PagingAndSortingParam;
import com.shopapp.admin.service.OrderService;
import com.shopapp.admin.service.SettingService;
import com.shopapp.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {

	private String defaultURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/orders")
	public String viewFirstPage() {
		return defaultURL;
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String viewByPage(@PathVariable("pageNum") Integer pageNum, 
			@PagingAndSortingParam(listName = "orders", moduleURL = "/orders") 
			PagingAndSortingHelper helper, HttpServletRequest request) {
		
		orderService.getByPage(pageNum, helper);
		loadCurrencySetting(request);
		
		return "orders/orders";
	}
	
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			orderService.delete(id);
			ra.addFlashAttribute("message", "The order has been deleted successfully");
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultURL;
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewDetailOrder(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra, HttpServletRequest request) {
		try {
			model.addAttribute("order", orderService.get(id));
			loadCurrencySetting(request);
			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultURL;
		}
	}
	
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for(Setting currency: currencySettings) {
			request.setAttribute(currency.getKey(), currency.getValue());
		}
	}
}

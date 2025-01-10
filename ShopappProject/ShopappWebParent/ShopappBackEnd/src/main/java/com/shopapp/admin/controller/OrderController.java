package com.shopapp.admin.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.exception.OrderNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.paging.PagingAndSortingParam;
import com.shopapp.admin.security.ShopappUserDetails;
import com.shopapp.admin.service.OrderService;
import com.shopapp.admin.service.SettingService;
import com.shopapp.common.entity.Setting;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.entity.order.OrderDetail;
import com.shopapp.common.entity.order.OrderTrack;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.enumm.OrderStatus;

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
			PagingAndSortingHelper helper, HttpServletRequest request,
			@AuthenticationPrincipal ShopappUserDetails loggedUser) {
		
		orderService.getByPage(pageNum, helper);
		loadCurrencySetting(request);
		
		if(!loggedUser.hasRole("Salesperson") && !loggedUser.hasRole("Admin") 
				&& loggedUser.hasRole("Shipper")) {
			return "orders/orders_shipper";
		}
		
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
			RedirectAttributes ra, HttpServletRequest request,
			@AuthenticationPrincipal ShopappUserDetails loggedUser) {
		try {
			loadCurrencySetting(request);
			boolean isVisibleForAdminOrSalesPerson = false;
			
			if(loggedUser.hasRole("Salesperson") || loggedUser.hasRole("Admin")) {
				isVisibleForAdminOrSalesPerson = true;
			}
			
			model.addAttribute("order", orderService.get(id));
			model.addAttribute("isVisibleForAdminOrSalesPerson", isVisibleForAdminOrSalesPerson);

			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultURL;
		}
	}
	
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, 
				RedirectAttributes ra, HttpServletRequest request) {
		
		try {
			Order order = orderService.get(id);
			
			model.addAttribute("countries", orderService.getAllCountries());
			model.addAttribute("order", order);
			model.addAttribute("pageTitle", "Edit order(ID:" + id + ")");
			System.out.println(order.getCity());
			return "orders/order_form";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultURL;
		}
	}
	
	@PostMapping("/orders/save")
	public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
		String countryName = request.getParameter("countryName");
		order.setCountry(countryName);
		
		updateProductDetails(order, request);
		updateProductTracks(order, request);
		
		orderService.save(order);
		
		ra.addFlashAttribute("message", "The order with id: " + order.getId() + " has been updated successfully!");
		
		
		return defaultURL;
	}
	
	private void updateProductTracks(Order order, HttpServletRequest request) {
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackNotes = request.getParameterValues("trackNote");
		
		List<OrderTrack> orderTracks = new ArrayList<>();
		for(int i = 0; i < trackIds.length; i++) {
			OrderTrack orderTrack = new OrderTrack();
			int trackId = parseStringToInt(trackIds[i]);
			
			if(trackId > 0) {
				orderTrack.setId(trackId);
			}
			
			orderTrack.setOrder(order);
			orderTrack.setNotes(trackNotes[i]);
			orderTrack.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			orderTrack.setUpdatedTimeOnForm(trackDates[i]);
			
			orderTracks.add(orderTrack);
		}
		order.setOrderTracks(orderTracks);
	}

	private void updateProductDetails(Order order, HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productCosts = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] shippingCosts = request.getParameterValues("productShippingCost");
		
		Set<OrderDetail> orderDetails = new HashSet<>();
		for(int i = 0; i < detailIds.length; i++) {
			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = parseStringToInt(detailIds[i]);
			
			if(detailId > 0) {
				orderDetail.setId(detailId);
			}
			
			orderDetail.setProductCost(parseStringToFloat(productCosts[i]));
			orderDetail.setSubtotal(parseStringToFloat(productSubtotals[i]));
			orderDetail.setShippingCost(parseStringToFloat(shippingCosts[i]));
			orderDetail.setUnitPrice(parseStringToFloat(productPrices[i]));
			orderDetail.setQuantity(parseStringToInt(quantities[i]));
			orderDetail.setOrder(order);
			
			orderDetail.setProduct(new Product(parseStringToInt(productIds[i])));
			
			orderDetails.add(orderDetail);
		}
		
		order.setOrderDetails(orderDetails);
	}

	private float parseStringToFloat(String numberAsString) {
		return Float.parseFloat(numberAsString);
	}
	
	private int parseStringToInt(String numberAsString) {
		return Integer.parseInt(numberAsString);
	}
	
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for(Setting currency: currencySettings) {
			request.setAttribute(currency.getKey(), currency.getValue());
		}
	}
}

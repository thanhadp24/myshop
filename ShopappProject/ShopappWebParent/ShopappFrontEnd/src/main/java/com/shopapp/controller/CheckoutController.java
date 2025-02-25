package com.shopapp.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.common.bean.CurrencySettingBag;
import com.shopapp.common.bean.EmailSettingBag;
import com.shopapp.common.bean.PaymentSettingBag;
import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.ShippingRate;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.enumm.PaymentMethod;
import com.shopapp.exception.PaypalApiException;
import com.shopapp.service.AddressService;
import com.shopapp.service.CheckoutService;
import com.shopapp.service.CustomerService;
import com.shopapp.service.OrderService;
import com.shopapp.service.PaypalService;
import com.shopapp.service.SettingService;
import com.shopapp.service.ShippingRateService;
import com.shopapp.service.ShoppingCartService;
import com.shopapp.utils.Utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {
	
	@Autowired private CheckoutService checkoutService;
	@Autowired private AddressService addressService;
	@Autowired private ShoppingCartService cartService;
	@Autowired private ShippingRateService rateService;
	@Autowired private CustomerService customerService;
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;
	@Autowired private PaypalService paypalService;

	@GetMapping("/checkout")
	public String viewCheckoutPage(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultByCustomer(customer);
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = rateService.getForAddress(defaultAddress);
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = rateService.getForCustomer(customer);
		}
		
		if(shippingRate == null) {
			return "redirect:/cart";
		}
		
		List<CartItem> cartItems = cartService.getCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String paypalClientId = paymentSettings.getClientId();
		
		model.addAttribute("paypalClientId", paypalClientId);
		model.addAttribute("customer", customer);
		model.addAttribute("currencyCode", settingService.getCurrencyCode());
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);
		
		return "checkout/checkout";
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		Customer customer = getAuthenticatedCustomer(request);

		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		Address defaultAddress = addressService.getDefaultByCustomer(customer);
		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			shippingRate = rateService.getForAddress(defaultAddress);
		} else {
			shippingRate = rateService.getForCustomer(customer);
		}
		
		List<CartItem> cartItems = cartService.getCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
		cartService.deleteByCustomer(customer);
		sendOrderConfirmationMail(request, createdOrder);
		
		return "checkout/order_completed";
	}
	
	@PostMapping("/process_paypal_order")
	public String processPaypalOrder(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
		String orderId = request.getParameter("orderId");
		String pageTitle = null;
		String message = null;
		
		try {
			boolean validateOrder = paypalService.validateOrder(orderId);
			if(validateOrder) {
				return placeOrder(request);
			}else {
				pageTitle = "Checkout failure";
				message = "ERROR: Transaction could not be completed because order information is invalid!";
			}
		} catch (PaypalApiException e) {
			pageTitle = "Checkout failure";
			message = "ERROR: Transaction failed due to error: " + e.getMessage();
		}
		
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("message", message);
		
		return "message";
	}

	private void sendOrderConfirmationMail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utils.mailSenderImpl(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		
		String toAddress = order.getCustomer().getEmail();
		String subject = emailSettings.getOrderSubject();
		String content = emailSettings.getOrderContent();
		
		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		DateFormat df = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
		String orderTime = df.format(order.getOrderTime());
		
		CurrencySettingBag currencySettings = settingService.getCurrencySettings();
		String totalAmount = Utils.formatCurrency(order.getTotal(), currencySettings);
		
		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]", String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]", orderTime);
		content = content.replace("[[shippingAddress]]", order.getShippingAddress());
		content = content.replace("[[total]]", totalAmount);
		content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
		
		helper.setText(content, true);
		mailSender.send(message);
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String customerEmail = Utils.getEmailOfAuthenticationCustomer(request);
		return customerService.getByEmail(customerEmail);
	}
}

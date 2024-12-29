package com.shopapp.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.common.bean.EmailSettingBag;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.exception.CustomerNotFoundException;
import com.shopapp.security.CustomerUserDetails;
import com.shopapp.security.oauth.CustomerOAuth2User;
import com.shopapp.service.CustomerService;
import com.shopapp.service.SettingService;
import com.shopapp.utils.Utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("countries", customerService.getAllCountries());
		model.addAttribute("customer", new Customer());
		model.addAttribute("pageTitle", "Registration Form");
		
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerifyMail(request, customer);
		
		model.addAttribute("pageTitle", "Register");
		
		return "/register/register_success";
	}
	
	@GetMapping("/verify")
	public String verifyAccount(String code, Model model) {
		boolean verified = customerService.verify(code);
		
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
	
	@GetMapping("/account_details")
	public String viewCustomerDetails(HttpServletRequest request, Model model) {
		
		String email = Utils.getEmailOfAuthenticationCustomer(request);
		
		Customer customer = customerService.getByEmail(email);
		
		model.addAttribute("customer", customer);
		model.addAttribute("countries", customerService.getAllCountries());
		
		return "customer/customer_form";
	}
	
	@PostMapping("/update_account_details")
	public String udpateAccount(Customer customer, HttpServletRequest request, 
			Model model, RedirectAttributes ra) {
		
		customerService.update(customer);
		updateName4Authentication(customer, request);
		
		ra.addFlashAttribute("message", "Your account has been updated successfully.");		
		return "redirect:/account_details";
	}
	
	@GetMapping("/forgot_password")
	public String forgotPassword() {
	
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		
		try {
			String token = customerService.updateResetPasswordToken(email);
			
			String siteURL = Utils.getSiteURL(request);
			
			String link = siteURL + "/reset_password?token=" + token;
			
			sendEmailForResetPassword(link, email);
			
			model.addAttribute("message", "We have sent a reset password link to "
					+ "your email.Please check your email.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("message", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("message", "Could not send email");
		}
		
		return "customer/forgot_password_form";
	}
	
	@GetMapping("/reset_password")
	public String resetPassword(String token, Model model) {
		
		Customer customer = customerService.getByResetToken(token);
		if(customer != null) {
			model.addAttribute("token", token);
			
		} else {
			model.addAttribute("pageTitle", "Invalid token");
			model.addAttribute("message", "Invalid token");
			return "message";
		}
		
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String resetPassword(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("pageTitle", "Reset password");
			model.addAttribute("title",  "Reset your password.");
			model.addAttribute("message",  "You have successfully change your password.");
			
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid token");
			model.addAttribute("message", "Invalid token");
			return "message";
		}
	}
	
	private void sendEmailForResetPassword(String link, String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSenderImpl = Utils.mailSenderImpl(emailSettings);
		
		String toAddress = email;
		String subject = "Here's the link to reset your password!";
		String content = "<p>Hello</p>"
				+ "<p>You have requested to reset the password.</p>"
				+ "<p>Click the link below to reset your password.</p>"
				+ "<p><a href= \"" + link + "\">Change your password</a></p>"
				+ "<br>"
				+ "<p>Ignore this email if you do remember you password or "
				+ "you have not made this request.</p>";
		
		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content, true);
		
		mailSenderImpl.send(message);
	}

	private void updateName4Authentication(Customer customer, HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		
		if(principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetails(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
		}else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oAuth2User = (CustomerOAuth2User) auth2AuthenticationToken.getPrincipal();
			String fullname = customer.getFullName();
			oAuth2User.setFullName(fullname);
		}
	}
	
	private CustomerUserDetails getCustomerUserDetails(Object principal) {
		CustomerUserDetails customerUserDetails = null;
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			customerUserDetails = (CustomerUserDetails) token.getPrincipal();
		}else if(principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			customerUserDetails = (CustomerUserDetails) token.getPrincipal();
		}
		
		return customerUserDetails;
	}

	

	private void sendVerifyMail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSenderImpl = Utils.mailSenderImpl(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		
		String urlVerify = Utils.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		
		content = content.replace("[[url]]", urlVerify);
		
		helper.setText(content, true);
		
		mailSenderImpl.send(message);
		
	}
}

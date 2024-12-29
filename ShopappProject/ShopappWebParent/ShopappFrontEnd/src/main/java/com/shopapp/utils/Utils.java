package com.shopapp.utils;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopapp.common.bean.EmailSettingBag;
import com.shopapp.security.oauth.CustomerOAuth2User;

import jakarta.servlet.http.HttpServletRequest;

public class Utils {
	
	public static String getSiteURL(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		return url.replace(request.getServletPath(), "");
	}
	
	public static JavaMailSenderImpl mailSenderImpl(EmailSettingBag settingBag) {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		
		mailSenderImpl.setHost(settingBag.getHost());
		mailSenderImpl.setPort(settingBag.getPort());
		mailSenderImpl.setUsername(settingBag.getUsername());
		mailSenderImpl.setPassword(settingBag.getPassword());
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", settingBag.getSmtpAuth());
		props.setProperty("mail.smtp.starttls.enable", settingBag.getSmtpSecured());
		
		mailSenderImpl.setJavaMailProperties(props);
		
		return mailSenderImpl;
	}
	
	public static String getEmailOfAuthenticationCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		if(principal == null) return null;
		
		String customerEmail = null;
		
		if(principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		}else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oAuth2User = (CustomerOAuth2User) auth2AuthenticationToken.getPrincipal();
			customerEmail = oAuth2User.getEmail();
		}
		
		return customerEmail;
	}
}

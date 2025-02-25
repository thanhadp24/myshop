package com.shopapp.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopapp.common.Constants;
import com.shopapp.service.SettingService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class SettingFilter implements Filter {

	@Autowired
	private SettingService service;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		
		String url = servletRequest.getRequestURL().toString();
		if(url.endsWith(".jpg") || url.endsWith(".css") || url.endsWith(".js")
				|| url.endsWith(".png")) {
			chain.doFilter(request, response);
			return;
		}
		
		service.getGeneralSettings().forEach(gs -> {
			request.setAttribute(gs.getKey(), gs.getValue());
		});
		
		request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
		
		chain.doFilter(request, response);
	}

}

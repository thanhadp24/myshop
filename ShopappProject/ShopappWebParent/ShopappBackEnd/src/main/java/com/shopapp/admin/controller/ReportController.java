package com.shopapp.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopapp.admin.service.SettingService;
import com.shopapp.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReportController {
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/reports")
	public String viewReport(HttpServletRequest request) {
		
		loadCurrencySetting(request);
		return "reports/reports";
	}
	
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for(Setting currency: currencySettings) {
			request.setAttribute(currency.getKey(), currency.getValue());
		}
	}
}

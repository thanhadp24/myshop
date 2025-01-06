package com.shopapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.common.bean.CurrencySettingBag;
import com.shopapp.common.bean.EmailSettingBag;
import com.shopapp.common.bean.PaymentSettingBag;
import com.shopapp.common.entity.Currency;
import com.shopapp.common.entity.Setting;
import com.shopapp.common.enumm.SettingCategory;
import com.shopapp.repository.CurrencyRepository;
import com.shopapp.repository.SettingRepository;
import com.shopapp.service.SettingService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SettingServiceImpl implements SettingService{

	@Autowired
	private SettingRepository settingRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Override
	public List<Setting> getGeneralSettings() {
		return settingRepository.findByTwoCategories(SettingCategory.CURRENCY, SettingCategory.GENERAL);
	}
	
	@Override
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATE));
		
		return new EmailSettingBag(settings);
	}
	
	@Override
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingBag(settings);
	}
	
	@Override
	public PaymentSettingBag getPaymentSettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingBag(settings);
	}
	
	@Override
	public String getCurrencyCode() {
		Setting setting = settingRepository.findByKey("CURRENCY_ID");
		Currency currency = currencyRepository.findById(Integer.parseInt(setting.getValue())).get();
		return currency.getCode();
	}
}

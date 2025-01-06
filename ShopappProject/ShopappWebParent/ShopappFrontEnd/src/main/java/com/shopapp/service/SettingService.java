package com.shopapp.service;

import java.util.List;

import com.shopapp.common.bean.CurrencySettingBag;
import com.shopapp.common.bean.EmailSettingBag;
import com.shopapp.common.bean.PaymentSettingBag;
import com.shopapp.common.entity.Setting;

public interface SettingService {
	
	List<Setting> getGeneralSettings();
	
	EmailSettingBag getEmailSettings();

	CurrencySettingBag getCurrencySettings();

	PaymentSettingBag getPaymentSettings();

	String getCurrencyCode();
}

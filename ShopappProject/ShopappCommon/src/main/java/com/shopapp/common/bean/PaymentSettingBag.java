package com.shopapp.common.bean;

import java.util.List;

import com.shopapp.common.entity.Setting;

public class PaymentSettingBag extends SettingBag{

	public PaymentSettingBag(List<Setting> settings) {
		super(settings);
	}


	public String getPaypalUrl() {
		return super.getValue("PAYPAL_API_BASE_URL");
	}
	
	public String getClientId() {
		return super.getValue("PAYPAL_API_CLIENT_ID");
	}
	
	public String getClientSecret() {
		return super.getValue("PAYPAL_API_CLIENT_SECRET");
	}
}

package com.shopapp.common.bean;

import java.util.List;

import com.shopapp.common.entity.Setting;

public class CurrencySettingBag extends SettingBag{

	public CurrencySettingBag(List<Setting> settings) {
		super(settings);
	}

	public String getSymbol() {
		return super.getValue("CURRENCY_SYMBOL");
	}
	
	public String getSymbolPos() {
		return super.getValue("CURRENCY_SYMBOL_POSITION");
	}
	
	public int getDecimalDigits() {
		return Integer.parseInt(super.getValue("DECIMAL_DIGITS"));
	}
	
	public String getDecimalPointType() {
		return super.getValue("DECIMAL_POINT_TYPE");
	}
	
	public String getThousandPointType() {
		return super.getValue("THOUSANDS_POINT_TYPE");
	}
}

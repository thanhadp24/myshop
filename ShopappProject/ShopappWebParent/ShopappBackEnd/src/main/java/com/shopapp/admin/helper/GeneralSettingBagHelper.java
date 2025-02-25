package com.shopapp.admin.helper;

import java.util.List;

import com.shopapp.common.bean.SettingBag;
import com.shopapp.common.entity.Setting;

public class GeneralSettingBagHelper extends SettingBag{

	public GeneralSettingBagHelper(List<Setting> settings) {
		super(settings);
	}
	
	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}

	public void updateSiteLogo(String logo) {
		super.update("SITE_LOGO", logo);
	}
}

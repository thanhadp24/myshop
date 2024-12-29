package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.helper.GeneralSettingBagHelper;
import com.shopapp.common.entity.Setting;

public interface SettingService {
	
	List<Setting> getAll();
	
	GeneralSettingBagHelper getGeneralSettings();
	
	List<Setting> getMailServers();
	
	List<Setting> getMailTemplates();
	
	public void saveAll(Iterable<Setting> settings);
}

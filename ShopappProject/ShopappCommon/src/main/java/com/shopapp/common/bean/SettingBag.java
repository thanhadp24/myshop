package com.shopapp.common.bean;

import java.util.List;

import com.shopapp.common.entity.Setting;

public class SettingBag {
	
	private List<Setting> settings;

	public SettingBag(List<Setting> settings) {
		this.settings = settings;
	}
	
	public Setting get(String key) {
		
		int idx = settings.indexOf(new Setting(key)); // depends on equals --> must be override equals
		
		if(idx >= 0) {
			return settings.get(idx);
		}
		
		return null;
	}
	
	public String getValue(String key) {
		Setting setting = get(key);
		if(setting != null) {
			return setting.getValue();
		}
		return null;
	}
	
	public void update(String key, String value) {
		Setting setting = get(key);
		if(setting != null && value != null) {
			setting.setValue(value);
		}
	}
	
	public List<Setting> getSettings(){
		return settings;
	}
}

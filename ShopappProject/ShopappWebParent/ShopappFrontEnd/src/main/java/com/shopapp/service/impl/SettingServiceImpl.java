package com.shopapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.common.bean.EmailSettingBag;
import com.shopapp.common.entity.Setting;
import com.shopapp.common.enumm.SettingCategory;
import com.shopapp.repository.SettingRepository;
import com.shopapp.service.SettingService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SettingServiceImpl implements SettingService{

	@Autowired
	private SettingRepository settingRepository;
	
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
}

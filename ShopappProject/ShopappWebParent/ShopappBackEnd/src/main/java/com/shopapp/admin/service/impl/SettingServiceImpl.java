package com.shopapp.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.admin.helper.GeneralSettingBagHelper;
import com.shopapp.admin.repository.SettingRepository;
import com.shopapp.admin.service.SettingService;
import com.shopapp.common.entity.Setting;
import com.shopapp.common.enumm.SettingCategory;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SettingServiceImpl implements SettingService{

	@Autowired
	private SettingRepository settingRepository;

	@Override
	public List<Setting> getAll() {
		return settingRepository.findAll();
	}
	
	@Override
	public GeneralSettingBagHelper getGeneralSettings() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBagHelper(settings);
	}
	
	@Override
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}
	
	@Override
	public List<Setting> getMailServers() {
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	@Override
	public List<Setting> getMailTemplates() {
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATE);
	}
}

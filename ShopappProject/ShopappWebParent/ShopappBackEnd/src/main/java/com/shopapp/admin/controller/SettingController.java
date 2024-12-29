package com.shopapp.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.helper.GeneralSettingBagHelper;
import com.shopapp.admin.repository.CurrencyRepository;
import com.shopapp.admin.service.SettingService;
import com.shopapp.admin.utils.FileUploadUtil;
import com.shopapp.common.entity.Currency;
import com.shopapp.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@GetMapping("/settings")
	public String viewSetting(Model model) {

		model.addAttribute("currencies", currencyRepository.findAllByOrderByNameAsc());
		
		for(Setting setting: settingService.getAll()) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		model.addAttribute("pageTitle", "Setting");
		
		return "settings/settings";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneral(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		
		GeneralSettingBagHelper settingBag = settingService.getGeneralSettings();
		
		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);
		updateSettingValuesFromForm(request, settingBag.getSettings());
		
		ra.addFlashAttribute("message", "General setting have been saved success");
		
		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServer(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailServers = settingService.getMailServers();
		updateSettingValuesFromForm(request, mailServers);
		
		ra.addFlashAttribute("message", "Mail server setting have been saved success");
		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplates(HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailTemplates = settingService.getMailTemplates();
		updateSettingValuesFromForm(request, mailTemplates);
		
		ra.addFlashAttribute("message", "Mail templates setting have been saved success");
		return "redirect:/settings";
	}

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBagHelper settingBag) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			
			settingBag.updateSiteLogo(value);
			 
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBagHelper settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> idResult = currencyRepository.findById(currencyId);
		
		if(idResult.isPresent()) {
			Currency currency = idResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> settings) {
		for(Setting setting: settings) {
			String value = request.getParameter(setting.getKey());
			if(value != null) {
				setting.setValue(value);
			}
		}
		
		settingService.saveAll(settings);
	}
	
}

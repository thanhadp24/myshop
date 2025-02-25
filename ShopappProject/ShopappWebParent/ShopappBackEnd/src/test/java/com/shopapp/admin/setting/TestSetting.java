package com.shopapp.admin.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.SettingRepository;
import com.shopapp.common.entity.Setting;
import com.shopapp.common.enumm.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestSetting {
	
	@Autowired 
	private SettingRepository repository;
	
	@Test
	public void testCreate() {
//		Setting setting = new Setting("SITE_NAME", "Shopme", SettingCategory.GENARAL);
		Setting logo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright @ 2024", SettingCategory.GENERAL);
		
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandPointType = new Setting("THOUNDSANDS_POINT_TYPE", "Comma", SettingCategory.CURRENCY);
		
		repository.saveAll(List.of(logo, copyright, currencyId, symbol, symbolPosition,
				decimalPointType, decimalDigits, thousandPointType));
	}
	
	@Test
	public void test() {
		repository.findByCategory(SettingCategory.GENERAL).forEach(System.out::println);
	}

}

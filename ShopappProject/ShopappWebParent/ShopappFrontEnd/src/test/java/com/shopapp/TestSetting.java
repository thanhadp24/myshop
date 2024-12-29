package com.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.enumm.SettingCategory;
import com.shopapp.repository.SettingRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestSetting {

	@Autowired
	private SettingRepository repository;
	
	@Test
	public void test() {
		repository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.MAIL_SERVER)
			.forEach(System.out::println);
	}
}

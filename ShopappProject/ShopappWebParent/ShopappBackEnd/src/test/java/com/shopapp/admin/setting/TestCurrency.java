package com.shopapp.admin.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.CurrencyRepository;
import com.shopapp.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestCurrency {

	@Autowired 
	private CurrencyRepository repository;
	
	@Test
	public void test() {
		
       Currency a = new Currency("United States Dollar", "$", "USD");
       Currency b = new Currency("British Pound", "£", "GPB");
       Currency c = new Currency("Vietnam Dong", "₫", "VND");
       Currency d = new Currency("Chinese Yuan", "¥", "CNY");
       Currency e = new Currency("Japanese Yen", "円", "JPY");
       Currency f = new Currency("Korean Won", "₩", "KRW");
	     
		
       repository.saveAll(List.of(a, b,c,d,e,f));
	}
	
	@Test
	public void get() {
		repository.findAllByOrderByNameAsc().forEach(System.out::println);
	}
}
	
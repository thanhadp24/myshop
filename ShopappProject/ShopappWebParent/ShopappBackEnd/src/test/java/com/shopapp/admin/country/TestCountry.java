package com.shopapp.admin.country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.CountryRepository;
import com.shopapp.admin.repository.StateRepository;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestCountry {

	@Autowired
	private CountryRepository repository;
	
	@Autowired
	private StateRepository stateRepository;
	
	
	@Test
	public void testCreate() {
		repository.save(new Country("Viet Nam", "VN"));
	}
	
	@Test
	public void testState() {
		Country country = new Country(2);
		stateRepository.save(new State("Ho Chi Minh", country));
	}
}
